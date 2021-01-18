package com.ddhigh.halia.chat.client.io;

import com.ddhigh.halia.chat.client.io.packet.*;
import com.ddhigh.halia.chat.client.io.protocol.MsgType;
import com.ddhigh.halia.chat.client.ui.LoginWindow;
import com.ddhigh.halia.chat.client.ui.PublicChatWindow;
import com.ddhigh.halia.chat.client.ui.RegisterWindow;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultChatListener implements ChatListener {
    private static final Logger logger = LoggerFactory.getLogger(DefaultChatListener.class);
    private static DefaultChatListener shared;

    public static DefaultChatListener shared() {
        if (shared == null) {
            synchronized (DefaultChatListener.class) {
                if (shared == null) {
                    shared = new DefaultChatListener();
                }
            }
        }
        return shared;
    }

    private SocketChannel ch;

    public void setChannel(SocketChannel ch) {
        this.ch = ch;
    }

    private DefaultChatListener() {
    }


    @Override
    public void onChannelActive(ChannelHandlerContext ctx) {
        logger.info("connected");
        var ping = new Ping();
        ctx.writeAndFlush(ping);
    }

    @Override
    public void onChannelInActive(ChannelHandlerContext ctx) {
        logger.info("disconnected");
        // todo 回到登录
    }

    @Override
    public void onExceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("exception caught", cause);
    }

    @Override
    public void onPong(ChannelHandlerContext ctx, Pong packet) {
        logger.info("onPong");
        LoginWindow.shared().onPong();
    }

    @Override
    public void onRegisterResp(ChannelHandlerContext ctx, RegisterResp packet) {
        logger.info("onRegisterResp {}", packet);
        RegisterWindow.shared().onRegisterResp(packet);
    }

    @Override
    public void onLoginResp(ChannelHandlerContext ctx, LoginResp packet) {
        logger.info("onLoginResp {}", packet);
        LoginWindow.shared().onLoginResp(packet);
    }

    @Override
    public void onPublicChatResp(ChannelHandlerContext ctx, PublicChatResp packet) {
        logger.info("onPublicChatResp {}", packet);
        PublicChatWindow.shared().onPublicChatResp(packet);
    }

    @Override
    public void onPublicChatMessage(ChannelHandlerContext ctx, PublicChatMessage packet) {
        logger.info("onPublicChatMessage {}", packet);
        PublicChatWindow.shared().onPublicChatMessage(packet);
    }

    @Override
    public void onUnAuthorize(ChannelHandlerContext ctx, UnAuthorization packet) {
        logger.info("onUnAuthorize");
    }

    public void doLogin(String username, String password) {
        var packet = new LoginReq(username, password);
        ch.writeAndFlush(packet);
    }

    public void doRegister(String username, String password, String nickname) {
        var packet = new RegisterReq(username, password, nickname);
        ch.writeAndFlush(packet);
    }

    public void doSendPublicChat(String text) {
        var packet = new PublicChatReq(MsgType.TEXT, text);
        ch.writeAndFlush(packet);
    }
}
