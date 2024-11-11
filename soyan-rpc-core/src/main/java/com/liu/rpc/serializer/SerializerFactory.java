package com.liu.rpc.serializer;

import cn.hutool.core.util.StrUtil;
import com.liu.rpc.spi.SpiLoader;


public class SerializerFactory {

    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取序列化器的实例
     * @param key 键值
     * @return  序列化器
     */
    public static Serializer getInstance(String key){
        if (StrUtil.isBlank(key)){
            return DEFAULT_SERIALIZER;
        }
        SpiLoader.load(Serializer.class);
        return SpiLoader.getInstance(Serializer.class,key);
    }
}
