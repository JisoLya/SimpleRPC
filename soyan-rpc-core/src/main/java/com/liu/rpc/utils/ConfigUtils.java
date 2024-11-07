package com.liu.rpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

public class ConfigUtils {

    /**
     * 加载配置环境
     * @param tClass
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }


    /**
     * 支持区分环境
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder builder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            builder.append("-").append(environment);
        }
        builder.append(".properties");
        Props props = new Props(builder.toString());
        return props.toBean(tClass, prefix);
    }
}
