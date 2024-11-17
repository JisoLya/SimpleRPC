package com.liu.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInfoRegister<T> {
    /**
     * 服务的名称
     */
    private String serviceName;
    /**
     * 实现类
     */
    private Class<? extends T> implClass;
}
