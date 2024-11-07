package com.liu.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcResponse implements Serializable {

    /**
     * 方法的响应结果
     */
    private Object data;

    /**
     * 方法响应的数据类型
     */
    private Class<?> dataType;

    /**
     * 方法返回的异常
     */
    private Exception exception;

    /**
     * 方法的响应信息
     */
    private String message;
}
