package com.liu.rpc.config;

import com.liu.rpc.fault.retry.RetryStrategyKeys;
import com.liu.rpc.fault.tolerant.TolerantKeys;
import com.liu.rpc.loadbalancer.LoadBalancer;
import com.liu.rpc.loadbalancer.LoadBalancerKeys;
import com.liu.rpc.serializer.SerializerKeys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RPC框架默认配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcConfig {
    /**
     * 框架名称
     */
    private String name = "Soyan-rpc";

    /**
     * 版本号
     */
    private String version = "1.0.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "127.0.0.1";

    /**
     * 服务端口号
     */
    private Integer serverPort = 8080;

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 失败重试策略
     */
    private String retryStrategy = RetryStrategyKeys.FIXED_TIME;

    /**
     * 失败容错机制
     */
    private String tolerantStrategy = TolerantKeys.FAIL_FAST;
}
