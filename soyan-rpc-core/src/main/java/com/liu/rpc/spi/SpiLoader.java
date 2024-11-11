package com.liu.rpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.liu.rpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpiLoader {
    /**
     * 存储已加载的类：接口名-> (key->实现类名)
     */
    private static Map<String, Map<String, String>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 对象缓存实例，类路径->对象实例，单例模式
     */
    private static Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 对象全类名加载
     */
    private static Map<String, String> nameCache = new ConcurrentHashMap<>();

    /**
     * 系统SPI目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义SPI目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = {RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /**
     * 动态加载的类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载所有类型
     */
    public static void loadAll() {
        log.info("加载所有SPI");
        for (Class<?> clazz : LOAD_CLASS_LIST) {
            load(clazz);
        }
    }

    /**
     * 获取某个接口的实例
     *
     * @param clazz 类
     * @param key   键值
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<T> clazz, String key) {
        String clazzName = clazz.getName();
        Map<String, String> classMap = loaderMap.get(clazzName);
        if (classMap == null) {
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型", clazzName));
        }
        if (!classMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader 的 %s 不存在的类型 key=%s", clazzName, key));
        }
        //获取到要加载的实现类型
        Class<?> implClass = null;
        try {
            implClass = Class.forName(classMap.get(key));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String implClassName = implClass.getName();

        if (!instanceCache.containsKey(implClassName)) {
            try {
                instanceCache.put(implClassName, implClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                String errMessage = String.format("%s类实例化失败", implClassName);
                throw new RuntimeException(errMessage, e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }


    public static void load(Class<?> loadClass) {
        log.info("加载类型为 {} 的SPI", loadClass.getName());
        //扫描路径，用户自定义的优先级高于系统SPI
//        HashMap<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
            //读取每一个资源文件
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strArray = line.split("=");
                        if (strArray.length > 1) {
                            String key = strArray[0];
                            String className = strArray[1];
                            nameCache.put(key, className);
//                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.error("SPI resource load error", e);
                }
            }
        }
        //缓存需要加载的类名
        loaderMap.put(loadClass.getName(), nameCache);
//        return keyClassMap;
    }
}
