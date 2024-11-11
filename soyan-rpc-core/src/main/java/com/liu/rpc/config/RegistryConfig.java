package com.liu.rpc.config;

import lombok.Data;

/**
 * 注册中心配置类
 */
@Data
public class RegistryConfig {
    /**
     * 注册中心名
     */
    private String registry = "etcd";

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 过期时间
     */
    private Long timeout = 10000L;
}
