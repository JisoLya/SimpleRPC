package com.liu.rpc.server.tcp;

import com.liu.rpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpTestServer implements HttpServer {

    @Override
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        // 处理请求
//        server.connectHandler(new TcpServerHandler());
        server.connectHandler(socket -> {
            socket.handler(buffer -> {
                String testMessage = "Hello, server!Hello, server!Hello, server!Hello, server!";
                int messageLength = testMessage.getBytes().length;
                //解决半包与粘包问题
                //每次读取固定的长度
                /**
                 * 实际情况是，在读取数据包的过程中，消息体是变长的，因此我们需要修改一下逻辑，将一次读取分为先读取头部，再读取数据部分！
                 */
                RecordParser recordParser = RecordParser.newFixed(messageLength);
                recordParser.setOutput(
                        buffer1 -> {
                            String str = new String(buffer1.getBytes());
                            if (testMessage.equals(str)){
                                System.out.println(str);
                                System.out.println("good");
                            }
                        }
                );
                socket.handler(recordParser);
            });
        });

        // 启动 TCP 服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                log.info("TCP server started on port " + port);
            } else {
                log.info("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new TcpTestServer().doStart(8888);
    }
}
