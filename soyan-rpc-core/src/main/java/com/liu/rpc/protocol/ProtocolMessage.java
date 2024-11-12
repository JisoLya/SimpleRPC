package com.liu.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考虑一下我们在传输数据的时候需要什么信息?
 * 1. 消息头
 * 2. 消息体
 *
 * 消息头中需要包含什么呢？
 * 1> 魔数(防止任何人都可以向服务器发送数据，相当于一种暗号)
 * 2> 协议号(通过协议版本号来确定消息体来如何解析)
 * 3> 序列化器(以什么方式来解析消息体数据)
 * 4> 消息体长度
 * 5> 请求的状态，由被调用方设置
 * 6> 请求Id
 * 7> 请求类型(请求/响应)
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    /**
     * 请求头
     */
    private Header header;
    /**
     * 请求体
     */
    private T body;

    @Data
    public static class Header{
        private byte magic;
        private byte version;
        private byte serializer;
        private long requestId;
        private byte type;
        private byte status;
        private int bodyLength;
    }
}
