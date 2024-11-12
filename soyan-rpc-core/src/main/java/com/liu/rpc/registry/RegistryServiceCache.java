package com.liu.rpc.registry;

import com.liu.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryServiceCache {
    /**
     * 服务缓存
     */
    Map<String, List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    /**
     * 写入缓存
     *
     * @param serviceKey       服务键值
     * @param serviceMetaInfos 服务元数据列表
     */
    void writeCache(String serviceKey, List<ServiceMetaInfo> serviceMetaInfos) {
//        System.out.println("写入缓存..." + serviceKey);
        this.serviceCache.put(serviceKey, serviceMetaInfos);
    }

    /**
     * 获取缓存数据
     *
     * @param serviceKey 服务键值
     * @return 服务元数据列表
     */
    List<ServiceMetaInfo> getCache(String serviceKey) {
        return this.serviceCache.get(serviceKey);
    }

    /**
     * 清除缓存
     *
     * @param serviceKey 服务键
     */
    void clearCache(String serviceKey) {
        this.serviceCache.remove(serviceKey);
    }
}
