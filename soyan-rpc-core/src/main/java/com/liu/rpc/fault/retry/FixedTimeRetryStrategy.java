package com.liu.rpc.fault.retry;

import com.github.rholder.retry.*;
import com.liu.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 固定时长重试策略：
 * 每隔3S重试
 * 重试3次
 */
@Slf4j
public class FixedTimeRetryStrategy implements RetryStrategy {
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {

        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)//出现任意错误都要重试
                .withWaitStrategy(WaitStrategies.fixedWait(4L, TimeUnit.SECONDS))//每3秒重试
                .withStopStrategy(StopStrategies.stopAfterAttempt(1 + 3))//第1次+3次重试
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        //
                        log.info("retry time: {}", attempt.getAttemptNumber());
                    }
                }).build();
        return retryer.call(callable);
    }
}
