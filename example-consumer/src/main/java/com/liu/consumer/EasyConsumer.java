package com.liu.consumer;

import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.utils.ConfigUtils;


public class EasyConsumer {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
