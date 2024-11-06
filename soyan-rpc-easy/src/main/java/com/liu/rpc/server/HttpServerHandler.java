package com.liu.rpc.server;

import com.liu.rpc.model.RpcRequest;
import com.liu.rpc.model.RpcResponse;
import com.liu.rpc.registry.LocalRegistry;
import com.liu.rpc.serializer.JdkSerializer;
import com.liu.rpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        //指定序列化器
        final Serializer serializer = new JdkSerializer();

        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;

            //解码字节数据封装到RpcRequest对象中
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                rpcResponse.setMessage("RpcRequest is null!");
                doResponse(request, rpcResponse, serializer);
                return;
            }
            try {
                Class<?> implClazz = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClazz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                Object result = method.invoke(implClazz.getDeclaredConstructor().newInstance(), rpcRequest.getParams());
                //封装返回数据
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                //出现错误也要正确返回
                e.printStackTrace();
                rpcResponse.setException(e);
                rpcResponse.setMessage(e.getMessage());
            }
            doResponse(request, rpcResponse, serializer);
        });
    }

    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse response = request.response().putHeader("content-type", "application/json");

        try {
            byte[] bytes = serializer.serialize(rpcResponse);
            response.end(Buffer.buffer(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            response.end(Buffer.buffer());
        }
    }
}
