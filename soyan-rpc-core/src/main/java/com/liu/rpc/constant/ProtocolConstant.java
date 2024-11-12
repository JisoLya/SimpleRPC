package com.liu.rpc.constant;

public interface ProtocolConstant {
    /**
     * 头部长度
     */
    int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 协议魔数
     */
    byte PROTOCOL_MAGIC = 0xf;

    /**
     * 协议版本号
     */
    byte PROTOCOL_VERSION = 0x01;
}
