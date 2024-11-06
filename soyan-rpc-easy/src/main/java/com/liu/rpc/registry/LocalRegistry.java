package com.liu.rpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistry {

    /**
     * 存储注册信息
     */
    private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     * @param serviceName 服务名称
     * @param clazz 服务的类
     */
    public static void registry(String serviceName,Class<?> clazz) {
        map.put(serviceName,clazz);
    }

    /**
     * 获取服务
     * @param serviceName 服务名称
     * @return 返回服务的类
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    /**
     * 删除某个服务
     * @param serviceName 服务名称
     */
    public static void remove(String serviceName) {
        map.remove(serviceName);
    }

}
