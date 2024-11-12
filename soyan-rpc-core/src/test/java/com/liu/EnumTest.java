package com.liu;

import com.liu.rpc.protocol.ProtocolMessageStatusEnum;
import com.liu.rpc.protocol.ProtocolMessageTypeEnum;
import io.vertx.core.buffer.Buffer;
import org.junit.Test;

import java.util.Arrays;

public class EnumTest {

    @Test
    public void test() {
        System.out.println(ProtocolMessageTypeEnum.getEnumByKey(0));
        System.out.println(ProtocolMessageStatusEnum.valueOf("OK"));
    }
    @Test
    public void test01(){
        Buffer buffer = Buffer.buffer();
        byte b = 0xf;
        long s = 11111L;
        buffer.appendLong(s);
        buffer.appendByte(b);

        System.out.println(buffer.getLong(0));
        System.out.println(buffer.getByte(8));
    }
}
