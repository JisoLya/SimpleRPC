package com.liu.example.provider;

import com.liu.example.common.service.UserService;
import com.liu.rpc.RpcApplication;
import com.liu.rpc.config.RegistryConfig;
import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.model.ServiceMetaInfo;
import com.liu.rpc.registry.LocalRegistry;
import com.liu.rpc.registry.Registry;
import com.liu.rpc.registry.RegistryFactory;
import com.liu.rpc.server.tcp.VertxTcpServer;

public class ExampleProvider {
    public static void main(String[] args) {
        //配置初始化
        RpcApplication.init();

        //服务的注册
        String serviceName = UserService.class.getName();
        LocalRegistry.registry(serviceName,UserServiceImpl.class);
        //注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        try {
            registry.register(serviceMetaInfo);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        //启动服务
        VertxTcpServer server = new VertxTcpServer();
        //监听配置端口
        server.doStart(rpcConfig.getServerPort());
    }
}
