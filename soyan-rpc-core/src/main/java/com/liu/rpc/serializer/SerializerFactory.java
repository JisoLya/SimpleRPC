package com.liu.rpc.serializer;

import com.liu.rpc.spi.SpiLoader;


public class SerializerFactory {
    static {
        SpiLoader.load(Serializer.class);
    }

    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取序列化器的实例
     * @param key 键值
     * @return  序列化器
     */
    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class,key);
    }
}
