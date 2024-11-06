package com.liu.consumer;

import com.liu.example.common.model.User;
import com.liu.example.common.service.UserService;


public class EasyConsumer {
    public static void main(String[] args) {
        UserService proxy = ServiceProxyFactory.getProxy(UserService.class);
        User jisoo = new User("Jisoo");
        proxy.getUser(jisoo);
        //TODO 需要获取通过Web服务来获取对象

    }
}
