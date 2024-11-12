package com.liu.rpc.protocol;

import com.liu.rpc.constant.ProtocolConstant;
import com.liu.rpc.model.RpcRequest;
import com.liu.rpc.model.RpcResponse;
import com.liu.rpc.serializer.Serializer;
import com.liu.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

public class ProtocolMessageDecoder {
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new RuntimeException("魔数非法！");
        }
        //Buffer内部根据字节的索引来寻找位置
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setRequestId(buffer.getLong(3));
        header.setType(buffer.getByte(11));
        header.setStatus(buffer.getByte(12));
        header.setBodyLength(buffer.getInt(13));

        //解决沾包问题
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());
        //获取序列化器
        ProtocolMessageSerialierEnum serializerEnum = ProtocolMessageSerialierEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("Decoder: 序列化器不存在！");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        //解码
        ProtocolMessageTypeEnum messageType = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if (messageType == null) {
            throw new RuntimeException("Decoder: 消息类型不存在");
        }
        switch (messageType) {
            case REQUEST:
                RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, rpcRequest);
            case RESPONSE:
                RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, rpcResponse);
            case HEART_BEAT:
            case OTHERS:
            default:
                System.out.println("不支持的消息类型");

        }

        return null;
    }
}
