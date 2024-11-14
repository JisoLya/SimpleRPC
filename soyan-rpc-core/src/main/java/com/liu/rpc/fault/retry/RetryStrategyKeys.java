package com.liu.rpc.fault.retry;

public interface RetryStrategyKeys {
    /**
     * 不重试
     */
    String NO = "no";
    /**
     * 固定时长
     */
    String FIXED_TIME = "fixedTime";
    /**
     * 指数增加
     */
    String EXPONENTIAL = "exponential";
}
