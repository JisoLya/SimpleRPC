package com.liu.rpc.fault.tolerant;

import cn.hutool.core.util.StrUtil;
import com.liu.rpc.spi.SpiLoader;

public class FaultTolerantFactory {
    private static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY = new FailFastStrategy();

    public static TolerantStrategy getInstance(String key) {
        if (StrUtil.isEmpty(key)) {
            return DEFAULT_TOLERANT_STRATEGY;
        }
        SpiLoader.load(TolerantStrategy.class);
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}
