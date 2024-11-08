package com.liu.rpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.setting.dialect.Props;
import org.dom4j.DocumentException;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;

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
        StringBuilder builder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            builder.append("-").append(environment);
        }
        //1. 查找带有application-environment前缀的文件
        String file = ConfigUtils.class.getClassLoader().getResource("").getFile();
        File target = new File(file);
        String configFileName = null;
        for (File f : target.listFiles()) {
            if (f.getName().startsWith("application")) {
                configFileName = f.getName();
            }
        }
        if (configFileName.endsWith(".properties")) {
            builder.append(".properties");
            Props props = new Props(builder.toString());

            return props.toBean(tClass, prefix);
        } else if (configFileName.endsWith(".xml")) {
            try {
                return (T) XmlReader.parseXmltoObject(file + configFileName,prefix);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } else if (configFileName.endsWith(".yaml")||configFileName.endsWith(".yml")) {
            try {
                return (T) YmlReader.parseYmlToObject(file+configFileName,prefix);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Undefined file suffix!");
        return null;

        //2. 分析其后缀，调用不同的文件读取器
        //3. 封装到Bean，返回
    }

}
