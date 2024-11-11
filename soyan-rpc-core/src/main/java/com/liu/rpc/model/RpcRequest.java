package com.liu.rpc.model;

import com.liu.rpc.constant.RpcConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcRequest implements Serializable {
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法的参数列表
     */
    private Object[] params;
    /**
     * 方法的参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 默认获取服务的版本号
     */
    @Builder.Default
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;
}
