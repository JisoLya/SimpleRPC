package com.liu.rpc.fault.tolerant;

import cn.hutool.core.collection.CollUtil;
import com.liu.rpc.RpcApplication;
import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.fault.retry.RetryStrategy;
import com.liu.rpc.fault.retry.RetryStrategyFactory;
import com.liu.rpc.loadbalancer.LoadBalancer;
import com.liu.rpc.loadbalancer.LoadBalancerFactory;
import com.liu.rpc.model.RpcRequest;
import com.liu.rpc.model.RpcResponse;
import com.liu.rpc.model.ServiceMetaInfo;
import com.liu.rpc.server.tcp.VertxTcpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 故障转移策略，调用其他节点的实现
 */
@Slf4j
public class FailOverStrategy implements TolerantStrategy {
    @Override
    public RpcResponse dpTolerant(Map<String, Object> context, Exception e) {
        System.out.println("FailOverStrategy dpTolerant");
        RpcRequest rpcRequest = (RpcRequest) context.get("rpcRequest");
        //获取请求
        List<ServiceMetaInfo> serviceInfoLists = (List<ServiceMetaInfo>) context.get("serviceInfoLists");
        ServiceMetaInfo serviceMetaInfo = (ServiceMetaInfo) context.get("serviceMetaInfo");


        removeFailNode(serviceInfoLists, serviceMetaInfo);

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        HashMap<String, Object> requestParameter = new HashMap<>();
        requestParameter.put("methodName", rpcRequest.getMethodName());

        RpcResponse response = null;
        while (CollUtil.isNotEmpty(serviceInfoLists) && response == null) {
            ServiceMetaInfo metaInfo = loadBalancer.select(requestParameter, serviceInfoLists);
            log.info("获取节点:{}", metaInfo);
            try {
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
                response = retryStrategy.doRetry(() -> VertxTcpClient.doResponse(rpcRequest, metaInfo));
                return response;
            } catch (Exception exception) {
                log.info("调用节点失败:{},删除失效节点", metaInfo);
                removeFailNode(serviceInfoLists, metaInfo);
                continue;
            }
        }
        throw new RuntimeException();
    }

    private void removeFailNode(List<ServiceMetaInfo> serviceInfoLists, ServiceMetaInfo serviceMetaInfo) {
        if (CollUtil.isNotEmpty(serviceInfoLists)) {
            Iterator<ServiceMetaInfo> iterator = serviceInfoLists.iterator();
            while (iterator.hasNext()) {
                ServiceMetaInfo next = iterator.next();
                if (serviceMetaInfo.getServiceNodeKey().equals(next.getServiceNodeKey())) {
                    iterator.remove();
                }
            }
        }
    }
}
