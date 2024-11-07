package com.liu.example.provider;

import com.liu.example.common.service.UserService;
import com.liu.rpc.RpcApplication;
import com.liu.rpc.registry.LocalRegistry;
import com.liu.rpc.server.VertxHttpServer;

public class ExampleProvider {
    public static void main(String[] args) {
        //配置初始化
        RpcApplication.init();

        //服务的注册
        LocalRegistry.registry(UserService.class.getName(),UserServiceImpl.class);

        VertxHttpServer server = new VertxHttpServer();
        server.doStart(8080);
    }
}
