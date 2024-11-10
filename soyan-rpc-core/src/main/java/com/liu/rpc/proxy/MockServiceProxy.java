package com.liu.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Class<?> returnType = method.getReturnType();
        log.info("Mock invoke:{}", method.getName());
        return getDefaultReturnType(returnType);
    }

    private Object getDefaultReturnType(Class<?> returnType) {
        if (returnType.isPrimitive()){
            //基本类型
            if(returnType == int.class){
                return 0;
            } else if (returnType == long.class) {
                return 0L;
            } else if (returnType == boolean.class) {
                return false;
            }else if (returnType == double.class) {
                return 0D;
            } else if (returnType == float.class) {
                return 0F;
            } else if (returnType == char.class) {
                return '\0';
            }else if(returnType == byte.class){
                return (byte) 0;
            }else if(returnType == short.class){
                return (short) 0;
            }
        }
        //其他类型
        return null;
    }
}
