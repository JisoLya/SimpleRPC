package com.liu.rpc.loadbalancer;

import cn.hutool.core.util.StrUtil;
import com.liu.rpc.spi.SpiLoader;

public class LoadBalancerFactory {

    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     * 获取负载均衡器
     * @param key 名称
     * @return
     */
    public static LoadBalancer getInstance(String key) {
        if(StrUtil.isEmpty(key)){
            return DEFAULT_LOAD_BALANCER;
        }
        SpiLoader.load(LoadBalancer.class);
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
