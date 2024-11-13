package com.liu.rpc.loadbalancer;

import com.liu.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {
    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList == null){
            System.out.println("服务元数据列表为空");
            return null;
        }
        //服务列表只有一个服务
        int size = serviceMetaInfoList.size();
        if (size == 1){
            return serviceMetaInfoList.get(0);
        }

        //随机获取一个服务
        return serviceMetaInfoList.get(random.nextInt(size));
    }
}
