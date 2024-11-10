package com.liu;

import cn.hutool.setting.dialect.Props;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class Test {
    @org.junit.Test
    public void test01() throws DocumentException, IOException {
        Props props = new Props();
        props.loadFromXML(new FileInputStream("E:\\GitRepository\\2024study\\RPCInfra\\soyan-rpc-core\\src\\test\\java\\com\\liu\\test.xml"));
        System.out.println(props);
    }

    @org.junit.Test
    public void test02() {
        Yaml yaml = new Yaml();
        File file = new File("E:\\GitRepository\\2024study\\RPCInfra\\soyan-rpc-core\\src\\test\\java\\com\\liu\\test.yaml");
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Map<String,Object> data = yaml.load(fileInputStream);
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @org.junit.Test
    public void test03(){
        Props prop = Props.getProp("E:\\GitRepository\\2024study\\RPCInfra\\soyan-rpc-core\\src\\test\\java\\com\\liu\\test.yaml");
        prop.store("test.properties");
        System.out.println(prop.toBean(Person.class));
    }
}
