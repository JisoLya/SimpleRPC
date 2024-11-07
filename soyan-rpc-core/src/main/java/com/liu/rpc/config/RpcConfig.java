package com.liu.rpc.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RPC框架默认配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcConfig {
    /**
     * 框架名称
     */
    private String name = "Soyan-rpc";

    /**
     * 版本号
     */
    private String version = "1.0.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "127.0.0.1";

    /**
     * 服务端口号
     */
    private Integer serverPort = 8080;
}
