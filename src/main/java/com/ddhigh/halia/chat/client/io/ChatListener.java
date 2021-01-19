package com.ddhigh.halia.chat.client.io;

import com.ddhigh.halia.chat.client.io.packet.*;
import io.netty.channel.ChannelHandlerContext;

public interface ChatListener {
    void onChannelActive(ChannelHandlerContext ctx);

    void onChannelInActive(ChannelHandlerContext ctx);

    void onExceptionCaught(ChannelHandlerContext ctx, Throwable cause);

    void onPong(ChannelHandlerContext ctx, Pong packet);

    void onRegisterResp(ChannelHandlerContext ctx, RegisterResp packet);

    void onLoginResp(ChannelHandlerContext ctx, LoginResp packet);

    void onPublicChatResp(ChannelHandlerContext ctx, PublicChatResp packet);

    void onPublicChatMessage(ChannelHandlerContext ctx, PublicChatMessage packet);

    void onUnAuthorize(ChannelHandlerContext ctx, UnAuthorization packet);

    void onFriendListResp(ChannelHandlerContext ctx, FriendListResp packet);

    void onFriendApplyResp(ChannelHandlerContext ctx, FriendApplyResp packet);

    void onFriendApplyResultResp(ChannelHandlerContext ctx, FriendApplyResultResp packet);

    void onFriendApplyMessage(ChannelHandlerContext ctx, FriendApplyMessage packet);

    void onFriendApplyResultMessage(ChannelHandlerContext ctx, FriendApplyResultMessage packet);

    void onSearchUserResp(ChannelHandlerContext ctx, SearchUserResp packet);

    void onPrivateChatResp(ChannelHandlerContext ctx, PrivateChatResp packet);

    void onPrivateChatMessage(ChannelHandlerContext ctx, PrivateChatMessage packet);
}
