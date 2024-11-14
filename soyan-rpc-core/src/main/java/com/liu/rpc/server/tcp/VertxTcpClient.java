package com.liu.rpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.liu.rpc.RpcApplication;
import com.liu.rpc.constant.ProtocolConstant;
import com.liu.rpc.model.RpcRequest;
import com.liu.rpc.model.RpcResponse;
import com.liu.rpc.model.ServiceMetaInfo;
import com.liu.rpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 先前所有的处理请求都放在了serviceProxy中，我们抽取一部分代码放在这个类中，serviceProxy只需要
 * 直接调用这个类的方法即可
 */
public class VertxTcpClient {
    public static RpcResponse doResponse(RpcRequest rpcRequest, ServiceMetaInfo metaInfo) throws ExecutionException, InterruptedException {
        //发送TCP请求
        Vertx vertx = Vertx.vertx();
        NetClient client = vertx.createNetClient();
        CompletableFuture<RpcResponse> future = new CompletableFuture<>();
        client.connect(metaInfo.getServicePort(), metaInfo.getServiceHost(),
                result -> {
                    if (!result.succeeded()) {
                        System.out.println("Failed to connect TCP server");
                        //重试机制依赖捕获异常来生效，因此在测试时，停掉example-provider的时候，服务器会链接失败，这时需要抛出异常才可以进行失败重试策略.
                        future.completeExceptionally(new RuntimeException("Failed to connect TCP server"));
                        return;
                    }
                    //构造请求
                    NetSocket socket = result.result();
                    ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                    ProtocolMessage.Header header = new ProtocolMessage.Header();
                    //设置请求头
                    header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                    header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                    header.setSerializer((byte) ProtocolMessageSerialierEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                    header.setRequestId(IdUtil.getSnowflakeNextId());
                    header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());

                    protocolMessage.setHeader(header);
                    protocolMessage.setBody(rpcRequest);

                    //编码请求
                    try {
                        Buffer buffer = ProtocolMessageEncoder.encode(protocolMessage);
                        socket.write(buffer);
                    } catch (IOException e) {
                        throw new RuntimeException("Client：协议消息编码错误");
                    }
                    TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                        try {
                            ProtocolMessage<RpcResponse> rpcResponseMessage =
                                    (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                            future.complete(rpcResponseMessage.getBody());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    socket.handler(tcpBufferHandlerWrapper);
                });
        RpcResponse rpcResponse = future.get();
        client.close();
        return rpcResponse;
    }
}