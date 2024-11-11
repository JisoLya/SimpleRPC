package com.liu.rpc;

import com.liu.rpc.registry.Registry;
import com.liu.rpc.config.RegistryConfig;
import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.constant.RpcConstant;
import com.liu.rpc.registry.RegistryFactory;
import com.liu.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newConfig) {
        rpcConfig = newConfig;
        log.info("RpcApplication init,config:{}", rpcConfig);
        //注册中心初始化

        RegistryConfig configRegistry = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(configRegistry.getRegistry());
        registry.init(configRegistry);
        log.info("Registry init,registry:{}", registry);
    }

    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e) {
            //加载失败，采取默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置信息
     * @return 返回配置信息
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            //此时配置空则获取配置信息
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
