package com.liu.rpc.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.liu.rpc.config.RpcConfig;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class PropertiesReader {
    public static <T> T getProps(String prefix, String fileName, Class<T> clazz) {
        if (fileName.endsWith(".properties")) {
            Props props = new Props(fileName);
            props.store("temp-application.properties");
        } else if (fileName.endsWith(".xml")) {
            //1. 创建XML读取器
            File file = FileUtil.file(fileName);
            SAXReader reader = new SAXReader();
            try {
                Document document = reader.read(file);
                Element root = document.getRootElement();
                Props props = new Props();
                parseElement(root, "", props);
                props.store("temp-application.properties");
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            //2. 转化为props对象
        } else if (fileName.endsWith(".yml") || fileName.endsWith(".yaml")) {
            File file = FileUtil.file(fileName);
            Yaml yaml = new Yaml();

            try (FileInputStream inputStream = new FileInputStream(file)) {
                Map<String, Object> data = yaml.load(inputStream);
                Props props = new Props();
                convertMaptoProps(data, "", props);
                props.store("temp-application.properties");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Props props = new Props("temp-application.properties");
        props.autoLoad(true);
        return props.toBean(clazz,prefix);
    }

    private static void parseElement(Element element, String prefix, Props props) {
        String key = prefix.isEmpty() ? element.getName() : prefix + "." + element.getName();
        String value = element.getTextTrim();

        for (Attribute attribute : element.attributes()) {
            props.setProperty(key + "." + attribute.getName(), attribute.getValue());
        }

        if (value.isEmpty() && !element.elements().isEmpty()) {
            for (Element child : element.elements()) {
                parseElement(child, key, props);
            }
        } else {
            props.setProperty(key, value);
        }
    }

    private static void convertMaptoProps(Map<String, Object> data, String prefix, Props props) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                convertMaptoProps((Map<String, Object>) value, key, props);
            } else {
                props.setProperty(key, value);
            }
        }
    }
}
