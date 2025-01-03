package com.liu.consumer;

import com.liu.example.common.model.User;
import com.liu.example.common.service.UserService;
import com.liu.rpc.bootstrap.ConsumerBootStrap;
import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.proxy.ServiceProxyFactory;
import com.liu.rpc.utils.ConfigUtils;


public class EasyConsumer {
    public static void main(String[] args) throws InterruptedException {

        ConsumerBootStrap.init();

        UserService service = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setUsername("liu");

        service.getUser(user);

        System.out.println("关闭服务");
        Thread.sleep(20*1000L);
        service.getUser(user);
    }
}
