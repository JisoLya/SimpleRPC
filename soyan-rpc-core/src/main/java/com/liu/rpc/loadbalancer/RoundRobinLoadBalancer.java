package com.liu.rpc.loadbalancer;

import com.liu.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    /**
     * 计数器，避免并发修改
     */
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList == null) {
            System.out.println("服务元数据列表为空");
            return null;
        }
        //列表只有一个服务
        int size = serviceMetaInfoList.size();
        if(size == 1){
            return serviceMetaInfoList.get(0);
        }
        //轮询
        int index = counter.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
