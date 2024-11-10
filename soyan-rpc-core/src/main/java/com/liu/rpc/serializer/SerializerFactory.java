package com.liu.rpc.serializer;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {
    public static final Map<String, Serializer> SERIALIZER_MAP = new HashMap<>() {
        {
            put(SerializerKeys.JDK, new JdkSerializer());
            put(SerializerKeys.JSON, new JsonSerializer());
            put(SerializerKeys.HESSIAN, new HessianSerializer());
            put(SerializerKeys.KRYO, new KryoSerializer());
        }
    };

    private static final Serializer DEFAULT_SERIALIZER = SERIALIZER_MAP.get("jdk");

    /**
     * 获取序列化器的实例
     * @param key 键值
     * @return  序列化器
     */
    public static Serializer getInstance(String key){
        return SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
    }
}
