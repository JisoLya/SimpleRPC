package com.liu.rpc.protocol;

import com.liu.rpc.serializer.Serializer;
import com.liu.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * 数据传输的方式：
 * 客户端 -> encoder -> 请求(buffer) -> 服务器
 *
 */
public class ProtocolMessageEncoder {
    public static Buffer encode(ProtocolMessage<?> message) throws IOException {
        if (message == null || message.getHeader() == null) {
            return Buffer.buffer();
        }
        //依次向Buffer中写入message的数据
        ProtocolMessage.Header header = message.getHeader();

        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendLong(header.getRequestId());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        //获取序列化器
        ProtocolMessageSerialierEnum serializerEnum = ProtocolMessageSerialierEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            System.out.println("Encoder: 序列化器不存在！");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] body = serializer.serialize(message.getBody());
        buffer.appendInt(body.length);
        buffer.appendBytes(body);
        return buffer;
    }
}
