package com.liu.example.common.service;

import com.liu.example.common.model.User;

public interface UserService {

    /**
     * 获取一个用户
     * @return 用户
     */
    User getUser(User user);

    default int getNumber(){
        return 1;
    }
}
