package com.liu.rpc.loadbalancer;

import cn.hutool.core.util.HashUtil;
import com.liu.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * TreeMap提供了ceilingEntry:获取给定大于或等于给定数值的最小键的条目
     * 符合我们一致性hash的需求
     */
    private final TreeMap<Integer, ServiceMetaInfo> treeMap = new TreeMap<>();

    /**
     * 虚节点数目，防止hash之后多个请求都打入同一个服务器中
     */
    private static final int VIRTUAL_NODE_NUMBER = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList == null) {
            return null;
        }
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUMBER; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                treeMap.put(hash, serviceMetaInfo);
            }
        }
        int paraHash = getHash(requestParams);
        Map.Entry<Integer, ServiceMetaInfo> entry = treeMap.ceilingEntry(paraHash);
        if (entry == null) {
            //没有大于等于键值的项,返回第一个
            entry = treeMap.firstEntry();
        }

        return entry.getValue();
    }

    /**
     * 需要给对象重写toString方法
     *
     * @param key 键值
     * @return 哈希
     */
    private int getHash(Object key) {
        return HashUtil.pjwHash(key.toString());
    }
}
