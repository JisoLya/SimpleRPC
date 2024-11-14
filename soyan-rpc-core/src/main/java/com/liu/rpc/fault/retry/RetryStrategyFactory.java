package com.liu.rpc.fault.retry;

import cn.hutool.core.util.StrUtil;
import com.liu.rpc.spi.SpiLoader;

public class RetryStrategyFactory {

    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NotRetryStrategy();

    public static RetryStrategy getInstance(String key) {
        SpiLoader.load(RetryStrategy.class);
        if (StrUtil.isEmpty(key)) {
            return DEFAULT_RETRY_STRATEGY;
        }
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }
}
