package com.liu.rpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.setting.dialect.Props;
import org.dom4j.DocumentException;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;
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
    public static <T> T loadConfig(Class<T> tClass, String prefix) throws DocumentException {
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
        //TODO 监听配置文件的变更
        //1. 查找带有application-environment前缀的文件
        String configFileName = getConfigFileName();
        return PropertiesReader.getProps(prefix,configFileName,tClass);
    }
    
    private static String getConfigFileName(){
        String file = Objects.requireNonNull(ConfigUtils.class.getClassLoader().getResource("")).getFile();
        File target = new File(file);
        String configFileName = "";
        for (File f : Objects.requireNonNull(target.listFiles())) {
            if (f.getName().startsWith("application")) {
                configFileName = f.getName();
            }
        }
        return configFileName;
    }
}
