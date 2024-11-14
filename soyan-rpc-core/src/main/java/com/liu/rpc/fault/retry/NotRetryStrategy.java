package com.liu.rpc.fault.retry;

import com.liu.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 不重试策略
 */
public class NotRetryStrategy implements RetryStrategy {
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
