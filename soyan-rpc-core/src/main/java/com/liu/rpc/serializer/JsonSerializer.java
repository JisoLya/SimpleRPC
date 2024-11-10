package com.liu.rpc.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liu.rpc.model.RpcRequest;
import com.liu.rpc.model.RpcResponse;

import java.io.IOException;

public class JsonSerializer implements Serializer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        T value = OBJECT_MAPPER.readValue(data, clazz);
        if (value instanceof RpcRequest) {
            return handleRequest((RpcRequest) value, clazz);
        }

        if (value instanceof RpcResponse) {
            return handleResponse((RpcResponse) value, clazz);
        }
        return value;
    }

    private <T> T handleResponse(RpcResponse response, Class<T> type) throws IOException {
        byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(response.getData());
        response.setData(OBJECT_MAPPER.readValue(bytes,response.getDataType()));
        return type.cast(response);
    }

    private <T> T handleRequest(RpcRequest request, Class<T> type) throws IOException {
        Class<?>[] paramTypes = request.getParamTypes();
        Object[] params = request.getParams();

        //循环处理多个参数类型
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> clazz = paramTypes[i];
            //不同的
            if (!clazz.isAssignableFrom(params[i].getClass())) {
                byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(params[i]);
                params[i] = OBJECT_MAPPER.readValue(bytes, clazz);
            }
        }

        return type.cast(request);
    }
}
