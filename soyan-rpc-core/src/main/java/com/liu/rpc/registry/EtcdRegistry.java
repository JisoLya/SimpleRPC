package com.liu.rpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.liu.rpc.config.RegistryConfig;
import com.liu.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class EtcdRegistry implements Registry {
    private Client client;
    private KV kvClient;

    private static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * 本机存储节点键的集合
     */
    private final Set<String> localRegistryNodeKeySet = new HashSet<>();

    /**
     * 注册中心缓存，支持多键值(消费端)
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    private final Set<String> watchingKeys = new ConcurrentHashSet<>();

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress()).connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        kvClient = client.getKVClient();
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //创建Lease和KV客户端
        log.info("服务注册...{}", serviceMetaInfo);
        Lease leaseClient = client.getLeaseClient();

        long leaseId = leaseClient.grant(30).get().getID();

        //设置存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();

        localRegistryNodeKeySet.add(registerKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registryKey, StandardCharsets.UTF_8));
        //从缓存中移除节点
        localRegistryNodeKeySet.remove(registryKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        log.info("服务发现:{}", serviceKey);
        String searchKey = ETCD_ROOT_PATH + serviceKey;
        //查找本地缓存
        List<ServiceMetaInfo> cache = registryServiceCache.getCache(serviceKey);
        if (CollUtil.isNotEmpty(cache)) {
            log.error("读取缓存");
            return cache;
        }
        log.info("从注册中心拉取列表");
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                    ByteSequence.from(searchKey, StandardCharsets.UTF_8),
                    getOption
            ).get().getKvs();
            List<ServiceMetaInfo> collect = keyValues.stream().map(
                    keyValue -> {
                        //开启对节点的监听
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        watch(key);

                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    }
            ).collect(Collectors.toList());
            //写入缓存
            this.registryServiceCache.writeCache(serviceKey, collect);
            return collect;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }

    }

    @Override
    public void destroy() {
        log.info("当前节点下线");
        for (String key : this.localRegistryNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key + " 节点下线失败", e);
            }
        }
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void heartBeat() {
        //Cron表达式
        CronUtil.schedule("*/10 * * * * * ", (Task) () -> {
            for (String key : localRegistryNodeKeySet) {
                //遍历存储的每一个键值
                try {
                    List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8)).get().getKvs();
                    //节点过期，需要重启节点
                    if (CollUtil.isEmpty(keyValues)) {
                        continue;
                    }
                    //节点未过期
                    KeyValue keyValue = keyValues.get(0);
                    String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                    ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                    register(serviceMetaInfo);
                } catch (Exception e) {
                    throw new RuntimeException(key + "续约失败", e);
                }
            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    /**
     * 监听服务列表事件
     */
    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();
        boolean newWatch = watchingKeys.add(serviceNodeKey);
        if (newWatch) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), watchResponse -> {
                for (WatchEvent event : watchResponse.getEvents()) {
                    switch (event.getEventType()) {
                        case DELETE:
//                            log.info("监听到删除事件,清除缓存");
                            this.registryServiceCache.clearCache(serviceNodeKey);
                        case PUT:
//                            log.info("监听到节点上线，调用时修改缓存");
                        default:
                            break;
                    }
                }
            });
        }
    }
}
