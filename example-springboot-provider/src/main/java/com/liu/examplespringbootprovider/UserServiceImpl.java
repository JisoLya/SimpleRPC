package com.liu.examplespringbootprovider;

import com.liu.example.common.model.User;
import com.liu.example.common.service.UserService;
import com.liu.soyanrpcspringbootstarter.annotations.RpcService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("注解驱动...用户名：" + user.getUsername());
        return user;
    }
}
