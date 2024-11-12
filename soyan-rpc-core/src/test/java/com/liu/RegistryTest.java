package com.liu;

import com.liu.example.common.model.User;
import com.liu.example.common.service.UserService;
import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.proxy.ServiceProxyFactory;
import com.liu.rpc.registry.Registry;
import com.liu.rpc.config.RegistryConfig;
import com.liu.rpc.model.ServiceMetaInfo;
import com.liu.rpc.registry.EtcdRegistry;
import com.liu.rpc.utils.ConfigUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * 注册中心测试
 */
public class RegistryTest {

    final Registry registry = new EtcdRegistry();

    @Before
    public void init() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }

    @Test
    public void register() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registry.register(serviceMetaInfo);
        serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1235);
        registry.register(serviceMetaInfo);
        serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("2.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registry.register(serviceMetaInfo);
    }

//    @Test
    public void unRegister() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registry.unRegister(serviceMetaInfo);
    }

    @Test
    public void serviceDiscovery() throws Exception {

    }

    @Test
    public void heartBeat() throws Exception {
        register();
        Thread.sleep(60*1000L);
    }

    @Test
    public void get() throws Exception {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
        //测试Mock逻辑，这里获取到了user = null,number = 0,走了给默认值的逻辑..
        UserService service = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setUsername("liu");
        service.getUser(user);
    }
}

