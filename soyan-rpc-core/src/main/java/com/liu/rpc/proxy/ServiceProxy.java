package com.liu.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.liu.rpc.RpcApplication;
import com.liu.rpc.constant.ProtocolConstant;
import com.liu.rpc.protocol.*;
import com.liu.rpc.registry.Registry;
import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.constant.RpcConstant;
import com.liu.rpc.model.RpcRequest;
import com.liu.rpc.model.RpcResponse;
import com.liu.rpc.model.ServiceMetaInfo;
import com.liu.rpc.registry.RegistryFactory;
import com.liu.rpc.serializer.Serializer;
import com.liu.rpc.serializer.SerializerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 在静态代理中，我们需要给每一个方法都创建一个代理，及其的不方便，我们可以进一步改进为动态代理技术
 * 动态代理
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        //获取序列化器
//        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .paramTypes(method.getParameterTypes())
                .params(args)
                .methodName(method.getName())
                .serviceName(method.getDeclaringClass().getName())
                .build();

        try {
            //从配置中心获取服务地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfos)) {
                throw new RuntimeException("暂无服务地址");
            }
            ServiceMetaInfo metaInfo = serviceMetaInfos.get(0);

            //不需要了,编码逻辑放在了Encoder里边
//            byte[] bodyBytes = serializer.serialize(rpcRequest);

            //发送TCP请求
            Vertx vertx = Vertx.vertx();
            NetClient client = vertx.createNetClient();
            CompletableFuture<RpcResponse> future = new CompletableFuture<>();
            client.connect(metaInfo.getServicePort(), metaInfo.getServiceHost(), result -> {
                if (result.succeeded()) {
                    //构造请求
                    NetSocket socket = result.result();
                    ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                    ProtocolMessage.Header header = new ProtocolMessage.Header();
                    //设置请求头
                    header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                    header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                    header.setSerializer((byte) ProtocolMessageSerialierEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                    header.setRequestId(IdUtil.getSnowflakeNextId());
                    header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());

                    protocolMessage.setHeader(header);
                    protocolMessage.setBody(rpcRequest);

                    //编码请求
                    try {
                        Buffer buffer = ProtocolMessageEncoder.encode(protocolMessage);
                        socket.write(buffer);
                    } catch (IOException e) {
                        throw new RuntimeException("协议消息编码错误");
                    }

//                    接受响应
                    socket.handler(buffer -> {
                        try {
                            ProtocolMessage<RpcResponse> message = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                            future.complete(message.getBody());
                        } catch (Exception e) {
                            throw new RuntimeException("消息协议解码错误");
                        }
                    });


                } else {
                    System.out.println("Failed to connect TCP server!");
                }
            });
            RpcResponse rpcResponse = future.get();
            return rpcResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
