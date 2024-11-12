package com.liu.rpc.server.tcp;


import com.liu.rpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetServer;

public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData) {
        return "Hello client! ".getBytes();
    }

    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        NetServer server = vertx.createNetServer();

        server.connectHandler(
                socket -> {
                    socket.handler(buffer -> {
                        byte[] requestData = buffer.getBytes();
                        byte[] responseData = handleRequest(requestData);
                        socket.write(Buffer.buffer(responseData));
                    });
                }
        );

        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server started on port " + port);
            }else {
                System.out.println("Server failed to start on port " + port);
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
