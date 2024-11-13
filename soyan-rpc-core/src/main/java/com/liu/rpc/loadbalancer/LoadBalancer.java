package com.liu.rpc.loadbalancer;

import com.liu.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器(消费端)
 */
public interface LoadBalancer {

    /**
     * 选择服务调用
     * @param requestParams 请求参数
     * @param serviceMetaInfoList 可选用的服务列表
     * @return serviceMetaInfo
     */
    ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
