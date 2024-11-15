package com.liu.rpc.fault.tolerant;

import com.liu.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错机制通用接口
 */
public interface TolerantStrategy {

    /**
     *
     * @param context 上下文，用于传递数据
     * @param e 异常
     * @return 降级的rpc响应
     */
    RpcResponse dpTolerant(Map<String, Object> context, Exception e);
}
