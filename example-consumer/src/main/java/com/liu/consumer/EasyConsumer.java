package com.liu.consumer;

import com.liu.rpc.config.RpcConfig;
import com.liu.rpc.utils.ConfigUtils;
import org.dom4j.DocumentException;


public class EasyConsumer {
    public static void main(String[] args) throws DocumentException {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
