package com.ddhigh.halia.chat.client.io;

import com.ddhigh.halia.chat.client.io.packet.*;
import com.ddhigh.halia.chat.client.io.protocol.MsgType;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultChatListener implements ChatListener {
    private static final Logger logger = LoggerFactory.getLogger(DefaultChatListener.class);

    @Override
    public void onChannelActive(ChannelHandlerContext ctx) {
        logger.info("connected");
        var ping = new Ping();
        ctx.writeAndFlush(ping);
    }

    @Override
    public void onChannelInActive(ChannelHandlerContext ctx) {
        logger.info("disconnected");
    }

    @Override
    public void onExceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("exception caught", cause);
    }

    @Override
    public void onPong(ChannelHandlerContext ctx, Pong packet) {
        logger.info("onPong");
        var registerReq = new RegisterReq("xialei1", "111111", "xialei1");
        ctx.writeAndFlush(registerReq);
    }

    @Override
    public void onRegisterResp(ChannelHandlerContext ctx, RegisterResp packet) {
        logger.info("onRegisterResp {}", packet);
        if (packet.getCode() != 0) {
            if (packet.getMessage().equals("账号已存在")) {
                doLogin(ctx);
            }
            return;
        }
        doLogin(ctx);
    }

    private void doLogin(ChannelHandlerContext ctx) {
        var loginReq = new LoginReq("xialei1", "111111");
        ctx.writeAndFlush(loginReq);
    }

    @Override
    public void onLoginResp(ChannelHandlerContext ctx, LoginResp packet) {
        logger.info("onLoginResp {}", packet);
        if (packet.getCode() != 0) {
            return;
        }
        var publicChatReq = new PublicChatReq(MsgType.TEXT, "HELLO FROM NETTY");
        ctx.writeAndFlush(publicChatReq);
    }

    @Override
    public void onPublicChatResp(ChannelHandlerContext ctx, PublicChatResp packet) {
        logger.info("onPublicChatResp {}", packet);
    }

    @Override
    public void onPublicChatMessage(ChannelHandlerContext ctx, PublicChatMessage packet) {
        logger.info("onPublicChatMessage {}", packet);
    }

    @Override
    public void onUnAuthorize(ChannelHandlerContext ctx, UnAuthorization packet) {
        logger.info("onUnAuthorize");
        doLogin(ctx);
    }
}
