package com.liu.rpc.fault.tolerant;

import com.liu.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败，通知上层调用者处理异常
 */
public class FailFastStrategy implements TolerantStrategy {

    @Override
    public RpcResponse dpTolerant(Map<String, Object> context, Exception e) {
        System.out.println("FailFastStrategy dpTolerant");
        throw new RuntimeException("服务调用失败！", e);
    }
}
