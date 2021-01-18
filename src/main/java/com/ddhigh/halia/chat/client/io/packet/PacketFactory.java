package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;

import java.util.HashMap;
import java.util.Map;

public class PacketFactory {
    private static final Map<Opcode, Class<? extends AbstractPacket>> map = new HashMap<>();

    static {
        map.put(Opcode.PING, Ping.class);
        map.put(Opcode.PONG, Pong.class);
        map.put(Opcode.RegisterReq, RegisterReq.class);
        map.put(Opcode.RegisterResp, RegisterResp.class);
        map.put(Opcode.LoginReq, LoginReq.class);
        map.put(Opcode.LoginResp, LoginResp.class);
        map.put(Opcode.PublicChatReq, PublicChatReq.class);
        map.put(Opcode.PublicChatResp, PublicChatResp.class);
        map.put(Opcode.PublicChatMessage, PublicChatMessage.class);
        map.put(Opcode.FriendListReq, FriendListReq.class);
        map.put(Opcode.FriendListResp, FriendListResp.class);
        map.put(Opcode.FriendApplyReq, FriendApplyReq.class);
        map.put(Opcode.FriendApplyResp, FriendApplyResp.class);
        map.put(Opcode.FriendApplyMessage, FriendApplyMessage.class);
        map.put(Opcode.FriendApplyResultReq, FriendApplyResultReq.class);
        map.put(Opcode.FriendApplyResultResp, FriendApplyResultResp.class);
        map.put(Opcode.FriendApplyResultMessage, FriendApplyResultMessage.class);
        map.put(Opcode.SearchUserResp, SearchUserResp.class);
    }

    public static Class<? extends AbstractPacket> getClass(Opcode opcode) {
        return map.get(opcode);
    }
}
