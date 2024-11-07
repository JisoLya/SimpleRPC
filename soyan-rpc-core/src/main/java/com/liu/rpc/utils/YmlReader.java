package com.liu.rpc.utils;

import com.liu.rpc.config.RpcConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class YmlReader {
    public static RpcConfig parseYmlToObject(String filepath,String prefix) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filepath);
        Map<String,Object> data = new Yaml().load(fileInputStream);

        Map<String,Object> rpc = (Map<String, Object>) data.get(prefix);
        String name = (String) rpc.get("name");
        String version = (String) rpc.get("version");
        String host = (String) rpc.get("host");
        Integer port = (Integer) rpc.get("port");
        return new RpcConfig(name,version,host,port);
    }
}
