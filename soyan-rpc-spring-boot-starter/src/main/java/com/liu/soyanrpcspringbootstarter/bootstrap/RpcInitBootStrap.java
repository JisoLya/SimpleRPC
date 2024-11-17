package com.liu.soyanrpcspringbootstarter.bootstrap;

import com.liu.rpc.RpcApplication;
import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.server.tcp.VertxTcpServer;
import com.liu.soyanrpcspringbootstarter.annotations.EnableRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;


public class RpcInitBootStrap implements ImportBeanDefinitionRegistrar {
    private static final Logger log = LoggerFactory.getLogger(RpcInitBootStrap.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName())
                .get("needServer");

        RpcApplication.init();
        final RpcConfig config = RpcApplication.getRpcConfig();
        if(needServer){
            VertxTcpServer tcpServer = new VertxTcpServer();
            tcpServer.doStart(config.getServerPort());
        }else {
            log.info("不启动Server");
        }
    }
}
