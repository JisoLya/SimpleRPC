package com.liu.example.provider;

import com.liu.example.common.service.UserService;
import com.liu.rpc.registry.LocalRegistry;
import com.liu.rpc.server.VertxHttpServer;

public class ServiceProvider {
    public static void main(String[] args) {
        //服务的注册
        LocalRegistry.registry(UserService.class.getName(),UserServiceImpl.class);

        VertxHttpServer server = new VertxHttpServer();
        server.doStart(8080);
    }
}
