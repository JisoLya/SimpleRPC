package com.liu.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.liu.rpc.RpcApplication;
import com.liu.rpc.registry.Registry;
import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.constant.RpcConstant;
import com.liu.rpc.model.RpcRequest;
import com.liu.rpc.model.RpcResponse;
import com.liu.rpc.model.ServiceMetaInfo;
import com.liu.rpc.registry.RegistryFactory;
import com.liu.rpc.serializer.Serializer;
import com.liu.rpc.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 在静态代理中，我们需要给每一个方法都创建一个代理，及其的不方便，我们可以进一步改进为动态代理技术
 * 动态代理
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        //获取序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .paramTypes(method.getParameterTypes())
                .params(args)
                .methodName(method.getName())
                .serviceName(method.getDeclaringClass().getName())
                .build();

        try{
            //从配置中心获取服务地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());

            if(CollUtil.isEmpty(serviceMetaInfos)){
                throw new RuntimeException("暂无服务地址");
            }
            ServiceMetaInfo metaInfo = serviceMetaInfos.get(0);
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result;
            //修改为了软编码，获取配置文件的信息..
            try(HttpResponse response = HttpRequest.post(metaInfo.getServiceAddress()).body(bytes).execute()) {
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
