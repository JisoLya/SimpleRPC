package com.liu.rpc.protocol;

import lombok.Getter;

@Getter
public enum ProtocolMessageSerialierEnum {
    JDK(0, "jdk"),
    JSON(1, "json"),
    KRYO(2, "kryo"),
    HESSIAN(3, "hessian");

    private final int key;
    private final String value;

    ProtocolMessageSerialierEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static ProtocolMessageSerialierEnum getEnumByKey(int key) {
        for (ProtocolMessageSerialierEnum e : ProtocolMessageSerialierEnum.values()) {
            if (e.key == key) {
                return e;
            }
        }
        return null;
    }

    public static ProtocolMessageSerialierEnum getEnumByValue(String value) {
        for (ProtocolMessageSerialierEnum e : ProtocolMessageSerialierEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
