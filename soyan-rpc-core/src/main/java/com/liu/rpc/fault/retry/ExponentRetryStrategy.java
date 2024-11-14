package com.liu.rpc.fault.retry;

import com.github.rholder.retry.*;
import com.liu.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExponentRetryStrategy implements RetryStrategy {
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                //第一个参数是以毫秒值为单位的
                .withWaitStrategy(WaitStrategies.exponentialWait(1000L, 36L, TimeUnit.SECONDS))//指数重试策略，达到36s失败
                .withStopStrategy(StopStrategies.stopAfterAttempt(1 + 4))
                .withRetryListener(new RetryListener() {

                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("retry attempt : {}", attempt.getAttemptNumber());
                    }
                }).build();
        return retryer.call(callable);
    }
}
