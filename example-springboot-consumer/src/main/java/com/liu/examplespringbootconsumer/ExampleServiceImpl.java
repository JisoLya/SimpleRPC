package com.liu.examplespringbootconsumer;

import com.liu.example.common.model.User;
import com.liu.example.common.service.UserService;
import com.liu.soyanrpcspringbootstarter.annotations.RpcReference;

import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setUsername("Soyan");
        User serviceUser = userService.getUser(user);

        System.out.println(serviceUser.getUsername());
    }
}
