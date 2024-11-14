package com.liu;

import com.liu.rpc.fault.retry.ExponentRetryStrategy;
import com.liu.rpc.fault.retry.FixedTimeRetryStrategy;
import com.liu.rpc.fault.retry.NotRetryStrategy;
import com.liu.rpc.fault.retry.RetryStrategy;
import com.liu.rpc.model.RpcResponse;
import org.junit.Test;

/**
 * 重试策略测试
 */
public class RetryStrategyTest {

    RetryStrategy retryStrategy = new ExponentRetryStrategy();

    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}
