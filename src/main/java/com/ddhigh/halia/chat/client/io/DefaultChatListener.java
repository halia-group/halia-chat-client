package com.ddhigh.halia.chat.client.io;

import com.ddhigh.halia.chat.client.io.packet.*;
import com.ddhigh.halia.chat.client.io.protocol.FriendStatus;
import com.ddhigh.halia.chat.client.io.protocol.MsgType;
import com.ddhigh.halia.chat.client.ui.LoginWindow;
import com.ddhigh.halia.chat.client.ui.MainWindow;
import com.ddhigh.halia.chat.client.ui.RegisterWindow;
import com.ddhigh.halia.chat.client.ui.SearchUserDialog;
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
    private SearchUserDialog searchUserDialog;

    public void setSearchUserDialog(SearchUserDialog searchUserDialog) {
        this.searchUserDialog = searchUserDialog;
    }

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
        MainWindow.shared().onPublicChatResp(packet);
    }

    @Override
    public void onPublicChatMessage(ChannelHandlerContext ctx, PublicChatMessage packet) {
        logger.info("onPublicChatMessage {}", packet);
        MainWindow.shared().onPublicChatMessage(packet);
    }

    @Override
    public void onUnAuthorize(ChannelHandlerContext ctx, UnAuthorization packet) {
        logger.info("onUnAuthorize");
    }

    @Override
    public void onFriendListResp(ChannelHandlerContext ctx, FriendListResp packet) {
        logger.info("{}", packet);
        MainWindow.shared().onFriendListResp(packet);
    }

    @Override
    public void onFriendApplyResp(ChannelHandlerContext ctx, FriendApplyResp packet) {
        logger.info("{}", packet);
        if (searchUserDialog != null) {
            searchUserDialog.onFriendApplyResp(packet);
        }
    }

    @Override
    public void onFriendApplyResultResp(ChannelHandlerContext ctx, FriendApplyResultResp packet) {
        logger.info("{}", packet);
        MainWindow.shared().onFriendApplyResultResp(packet);
    }

    @Override
    public void onFriendApplyMessage(ChannelHandlerContext ctx, FriendApplyMessage packet) {
        logger.info("{}", packet);
        MainWindow.shared().onFriendApplyMessage(packet);
    }

    @Override
    public void onFriendApplyResultMessage(ChannelHandlerContext ctx, FriendApplyResultMessage packet) {
        logger.info("{}", packet);
        MainWindow.shared().onFriendApplyResultMessage(packet);
    }

    @Override
    public void onSearchUserResp(ChannelHandlerContext ctx, SearchUserResp packet) {
        logger.info("{}", packet);
        if (searchUserDialog != null) {
            searchUserDialog.onSearchUserResp(packet);
        }
    }

    @Override
    public void onPrivateChatResp(ChannelHandlerContext ctx, PrivateChatResp packet) {
        logger.info("{}", packet);
        MainWindow.shared().onPrivateChatResp(packet);
    }

    @Override
    public void onPrivateChatMessage(ChannelHandlerContext ctx, PrivateChatMessage packet) {
        logger.info("{}", packet);
        MainWindow.shared().onPrivateChatMessage(packet);
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

    public void doFriendListReq() {
        var packet = new FriendListReq();
        ch.writeAndFlush(packet);
    }

    public void doFriendAccept(int senderId) {
        var packet = new FriendApplyResultReq(senderId, FriendStatus.ACCEPT, "");
        ch.writeAndFlush(packet);
    }

    public void doFriendReject(int senderId, String reason) {
        var packet = new FriendApplyResultReq(senderId, FriendStatus.REJECT, reason);
        ch.writeAndFlush(packet);
    }

    public void doSearchUser(String nickname) {
        var packet = new SearchUserReq(nickname);
        ch.writeAndFlush(packet);
    }

    public void doFriendApply(int userId, String reason) {
        var packet = new FriendApplyReq(userId, reason);
        ch.writeAndFlush(packet);
    }

    public void doPrivateChat(int receiverId, MsgType msgType, String message) {
        var packet = new PrivateChatReq(receiverId, msgType, message);
        ch.writeAndFlush(packet);
    }
}
