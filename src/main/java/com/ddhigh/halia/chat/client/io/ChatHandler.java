package com.ddhigh.halia.chat.client.io;

import com.ddhigh.halia.chat.client.io.packet.*;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChatHandler extends ChannelInboundHandlerAdapter {
    private final ChatListener listener;

    public ChatHandler() {
        this.listener = DefaultChatListener.shared();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        listener.onChannelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        listener.onChannelInActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        listener.onExceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        var packet = (AbstractPacket) msg;
        var opcode = packet.getClass().getAnnotation(Packet.class).opcode();
        switch (opcode) {
            case PONG:
                listener.onPong(ctx, (Pong) packet);
                break;
            case RegisterResp:
                listener.onRegisterResp(ctx, (RegisterResp) packet);
                break;
            case LoginResp:
                listener.onLoginResp(ctx, (LoginResp) packet);
                break;
            case PublicChatResp:
                listener.onPublicChatResp(ctx, (PublicChatResp) packet);
                break;
            case PublicChatMessage:
                listener.onPublicChatMessage(ctx, (PublicChatMessage) packet);
                break;
            case UnAuthorization:
                listener.onUnAuthorize(ctx, (UnAuthorization) packet);
                break;
            case FriendListResp:
                listener.onFriendListResp(ctx, (FriendListResp) packet);
                break;
            case FriendApplyResp:
                listener.onFriendApplyResp(ctx, (FriendApplyResp) packet);
                break;
            case FriendApplyResultResp:
                listener.onFriendApplyResultResp(ctx, (FriendApplyResultResp) packet);
                break;
            case FriendApplyMessage:
                listener.onFriendApplyMessage(ctx, (FriendApplyMessage) packet);
                break;
            case FriendApplyResultMessage:
                listener.onFriendApplyResultMessage(ctx, (FriendApplyResultMessage) packet);
                break;
            case SearchUserResp:
                listener.onSearchUserResp(ctx, (SearchUserResp) packet);
                break;
            case PrivateChatResp:
                listener.onPrivateChatResp(ctx, (PrivateChatResp) packet);
                break;
            case PrivateChatMessage:
                listener.onPrivateChatMessage(ctx, (PrivateChatMessage) packet);
                break;
        }
    }
}
