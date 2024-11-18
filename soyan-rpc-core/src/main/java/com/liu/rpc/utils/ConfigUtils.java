package com.liu.rpc.utils;

import cn.hutool.core.io.resource.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;

import java.io.IOException;
import java.util.Objects;

public class ConfigUtils {

    /**
     * 加载配置环境
     *
     * @param tClass
     * @param prefix
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 支持区分环境
     *
     * @param tClass
     * @param prefix
     * @param environment
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        //1. 查找带有application-environment前缀的文件
        String configFileName = getConfigFileName();
        return PropertiesReader.getProps(prefix, configFileName, tClass);
    }

    private static String getConfigFileName() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String pattern = "classpath*:application*";
        org.springframework.core.io.Resource[] resources = null;
        try {
            resources = resolver.getResources(pattern);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return resources[0].getFilename();
    }
}
