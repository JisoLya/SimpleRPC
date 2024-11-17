package com.liu.soyanrpcspringbootstarter.annotations;

import com.liu.rpc.constant.RpcConstant;
import com.liu.rpc.fault.retry.RetryStrategyKeys;
import com.liu.rpc.fault.tolerant.TolerantKeys;
import com.liu.rpc.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务消费者所需要的注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcReference {
    /**
     * 服务接口类
     *
     * @return
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 版本号
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 负载均衡器
     */
    String loadBalancer() default LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    String retryStrategy() default RetryStrategyKeys.NO;

    /**
     * 容错措施
     */
    String tolerantStrategy() default TolerantKeys.FAIL_FAST;

    /**
     * 模拟调用
     */
    boolean mock() default false;
}
