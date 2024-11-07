package com.liu.rpc.utils;

import com.liu.rpc.config.RpcConfig;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

public class XmlReader {
    public static RpcConfig parseXMLtoObject(String filepath,String prefix) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(filepath));
        Element root = document.getRootElement();
        Element rpcConf = root.element(prefix);

        String name = rpcConf.elementText("name");
        String version = rpcConf.elementText("version");
        String host = rpcConf.elementText("host");
        String port = rpcConf.elementText("port");

        return new RpcConfig(name,version,host,Integer.parseInt(port));
    }
}
