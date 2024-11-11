package com.liu.rpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class ServiceMetaInfo {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion = "1.0";

    /**
     * 服务的地址
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;

    /**
     * 服务分组(未实现)
     */
    private String serviceGroup = "default";

    /**
     * 获取服务键名
     * @return 服务的键名 serviceName:serviceVersion
     */
    public String getServiceKey(){
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * 获取服务注册节点键名
     * @return 服务注册节点键名 serviceName:serviceVersion/serviceHost:servicePort
     */
    public String getServiceNodeKey(){
        return String.format("%s/%s:%s",getServiceKey(),serviceHost,servicePort);
    }

    /**
     * 获取服务的地址
     * @return 服务地址的字符串
     */
    public String getServiceAddress(){
        if(!StrUtil.contains(serviceHost,"http")){
            return String.format("http://%s:%s",serviceHost,servicePort);
        }
        return String.format("%s:%s",serviceHost,servicePort);
    }
}
