package com.liu.rpc.utils;

import cn.hutool.setting.dialect.Props;

public class PropertiesReader {
    public static <T> T getProps(String prefix, String fileName, Class<T> clazz) {

        if (fileName.endsWith(".properties")) {
            Props props = new Props(fileName);
            return props.toBean(clazz, prefix);
        } else if (fileName.endsWith(".xml")) {
            //1. 创建XML读取器
            //2. 转化为props对象

        } else if (fileName.endsWith(".yml") || fileName.endsWith(".yaml")) {
            Props props = new Props(fileName);
            return props.toBean(clazz, prefix);
        }
        return null;
    }
}
