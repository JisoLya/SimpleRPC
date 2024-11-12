package com.liu.rpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

public class VertxTcpClient {
    public void start(){
        Vertx vertx = Vertx.vertx();
        NetClient client = vertx.createNetClient();

        client.connect(8888,"localhost",result->{
            if(result.succeeded()){
                System.out.println("Connected to server!");
                NetSocket socket = result.result();

                socket.write("Hello server!");
                socket.handler(buffer -> {
                    System.out.println("Received response from server:" + buffer.toString());
                });
            }else {
                System.out.println("Failed to connect to server!");
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
