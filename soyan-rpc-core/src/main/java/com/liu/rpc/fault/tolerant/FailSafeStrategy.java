package com.liu.rpc.fault.tolerant;

import com.liu.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 静默处理，不做任何响应，直接返回空的response
 */
public class FailSafeStrategy implements TolerantStrategy{
    @Override
    public RpcResponse dpTolerant(Map<String, Object> context, Exception e) {
        System.out.println("FailSafeStrategy dpTolerant");
        return new RpcResponse();
    }
}
