package com.liu.rpc.bootstrap;

import com.liu.rpc.RpcApplication;
import com.liu.rpc.config.RegistryConfig;
import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.model.ServiceInfoRegister;
import com.liu.rpc.model.ServiceMetaInfo;
import com.liu.rpc.registry.LocalRegistry;
import com.liu.rpc.registry.Registry;
import com.liu.rpc.registry.RegistryFactory;
import com.liu.rpc.server.tcp.VertxTcpServer;


import java.util.List;

public class ProviderBootStrap {
    public static void init(List<ServiceInfoRegister<?>> serviceInfoRegisterList) {
        RpcApplication.init();

        final RpcConfig config = RpcApplication.getRpcConfig();

        for (ServiceInfoRegister<?> serviceInfoRegister : serviceInfoRegisterList) {
            String serviceName = serviceInfoRegister.getServiceName();
            //本地注册
            LocalRegistry.registry(serviceName, serviceInfoRegister.getImplClass());

            //注册到注册中心
            RegistryConfig registryConfig = config.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(config.getServerHost());
            serviceMetaInfo.setServicePort(config.getServerPort());

            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + "服务注册失败", e);
            }
        }

        VertxTcpServer tcpServer = new VertxTcpServer();
        tcpServer.doStart(config.getServerPort());
    }
}
