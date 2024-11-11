package com.liu.rpc.registry;

import com.liu.rpc.spi.SpiLoader;

public class RegistryFactory {

    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getInstance(String key) {
        SpiLoader.load(Registry.class);
        if (key == null) {
            return DEFAULT_REGISTRY;
        }
        return SpiLoader.getInstance(Registry.class, key);
    }
}
