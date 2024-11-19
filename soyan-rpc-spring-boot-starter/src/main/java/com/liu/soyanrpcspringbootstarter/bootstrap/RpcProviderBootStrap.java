package com.liu.soyanrpcspringbootstarter.bootstrap;

import com.liu.rpc.RpcApplication;
import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.model.ServiceMetaInfo;
import com.liu.rpc.registry.LocalRegistry;
import com.liu.rpc.registry.Registry;
import com.liu.rpc.registry.RegistryFactory;
import com.liu.soyanrpcspringbootstarter.annotations.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 服务提供者的启动类RpcProviderBootStrap
 * 作用：获取到所有包含@RpcService的类，并且通过注解的属性和反射机制，获取到要注册的
 * 服务信息，完成服务的注册。
 * 那么要怎样获取具有@RpcService注解的类呢?可以主动扫描包，也可以利用Spring的特性来监听Bean的加载
 * 我们直接实现BeanPostProcessor接口的postProcessAfterInitialization，就可以在某个加有
 * 注解@RpcService的Bean初始化之后执行注册操作
 */
public class RpcProviderBootStrap implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);

        if (rpcService != null) {
            Class<?> interfaceClass = rpcService.interfaceClass();
            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }

            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();

            //本地注册
            LocalRegistry.registry(serviceName, beanClass);
            //注册中心注册
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo metaInfo = new ServiceMetaInfo();
            metaInfo.setServiceName(serviceName);
            metaInfo.setServiceVersion(serviceVersion);
            metaInfo.setServiceHost(rpcConfig.getServerHost());
            metaInfo.setServicePort(rpcConfig.getServerPort());

            try {
                registry.register(metaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + "服务注册失败", e);
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean,beanName);
    }
}
