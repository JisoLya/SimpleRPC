package com.liu.rpc.fault.retry;

import com.liu.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

public interface RetryStrategy {

    /**
     * 重试机制
     * @param callable 任务
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
