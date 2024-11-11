package com.liu.rpc.registry;

import com.liu.rpc.config.RegistryConfig;
import com.liu.rpc.model.ServiceMetaInfo;

import java.util.List;

public interface Registry {
    /**
     * 初始化
     * @param registryConfig 注册中心配置
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务(服务端)
     * @param serviceMetaInfo 服务元数据
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务
     * @param serviceMetaInfo 服务元数据
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务发现(获取某个服务的所有节点)消费端
     * @param serviceKey 服务键名
     * @return 服务列表
     *
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey) throws Exception;

    /**
     * 服务销毁
     */
    void destroy();
}
