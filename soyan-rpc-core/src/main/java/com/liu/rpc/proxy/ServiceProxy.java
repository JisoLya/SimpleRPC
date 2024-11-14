package com.liu.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.liu.rpc.RpcApplication;
import com.liu.rpc.constant.ProtocolConstant;
import com.liu.rpc.fault.retry.RetryStrategy;
import com.liu.rpc.fault.retry.RetryStrategyFactory;
import com.liu.rpc.loadbalancer.LoadBalancer;
import com.liu.rpc.loadbalancer.LoadBalancerFactory;
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
import com.liu.rpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
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

            //负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            HashMap<String, Object> requestPara = new HashMap<>();
            requestPara.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo metaInfo = loadBalancer.select(requestPara, serviceMetaInfos);

            // 这里一次失败我们直接抛出错误，但是获取不到服务有很多可能：网络故障，或者服务提供者重启
            // 等等，在这些情况下，服务过一段时间就可以重新获取，我们需要给这段代码添加重试机制。

            //重试机制
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            RpcResponse rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doResponse(rpcRequest, metaInfo));
            //不需要了,编码逻辑放在了Encoder里边
//            byte[] bodyBytes = serializer.serialize(rpcRequest);

            return rpcResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
