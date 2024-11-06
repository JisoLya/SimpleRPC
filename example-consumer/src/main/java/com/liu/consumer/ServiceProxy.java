package com.liu.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.liu.rpc.model.RpcRequest;
import com.liu.rpc.model.RpcResponse;
import com.liu.rpc.serializer.JdkSerializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 在静态代理中，我们需要给每一个方法都创建一个代理，及其的不方便，我们可以进一步改进为动态代理技术
 * 动态代理
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        JdkSerializer serializer = new JdkSerializer();

        RpcRequest rpcRequest = RpcRequest.builder()
                .paramTypes(method.getParameterTypes())
                .params(args)
                .methodName(method.getName())
                .serviceName(method.getDeclaringClass().getName())
                .build();

        try{
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result;
            try(HttpResponse response = HttpRequest.post("http://localhost:8080").body(bytes).execute()) {
                result = response.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return rpcResponse.getData();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
