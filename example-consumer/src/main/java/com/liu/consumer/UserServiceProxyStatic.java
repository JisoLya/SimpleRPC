package com.liu.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.liu.example.common.model.User;
import com.liu.example.common.service.UserService;
import com.liu.rpc.model.RpcRequest;
import com.liu.rpc.model.RpcResponse;
import com.liu.rpc.serializer.JdkSerializer;

import java.io.IOException;

/**
 * UserService的静态代理
 */
public class UserServiceProxyStatic implements UserService {
    @Override
    public User getUser(User user) {
        //1. 构造序列化器
        JdkSerializer serializer = new JdkSerializer();
        //2. 构造请求体
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName("com.liu.example.common.service.UserService")
                .methodName("getUser")
                .params(new Object[]{user})
                .paramTypes(new Class[]{User.class})
                .build();
        //3. 序列化请求体
        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try (HttpResponse response = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                result = response.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
             e.printStackTrace();
        }
        return null;
    }
}
