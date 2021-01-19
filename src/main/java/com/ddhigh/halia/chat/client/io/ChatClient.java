package com.ddhigh.halia.chat.client.io;

import com.ddhigh.halia.chat.client.io.protocol.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatClient {
    private static final Logger logger = LoggerFactory.getLogger(ChatClient.class);
    private static ChatClient shared;

    public static ChatClient shared() {
        if (shared == null) {
            synchronized (ChatClient.class) {
                if (shared == null) {
                    shared = new ChatClient();
                }
            }
        }
        return shared;
    }

    private String host = "localhost";
    private int port;

    private ChatClient() {
        host = System.getProperty("server.host", host);
        String port = System.getProperty("server.port", "8080");
        try {
            this.port = Integer.parseInt(port, 10);
        } catch (NumberFormatException e) {
            this.port = 8080;
        }
        logger.info("host={},port={}", host, port);
    }

    public void bootstrap() throws InterruptedException {
        var group = new NioEventLoopGroup();
        try {
            var b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            DefaultChatListener.shared().setChannel(ch);
                            ch.pipeline().addLast("codec", new MessageCodec());
                            ch.pipeline().addLast("handler", new ChatHandler());
                        }
                    });
            var f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
