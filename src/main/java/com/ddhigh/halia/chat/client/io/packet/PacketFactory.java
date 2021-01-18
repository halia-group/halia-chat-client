package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;

import java.util.HashMap;
import java.util.Map;

public class PacketFactory {
    private static Map<Opcode, Class<? extends AbstractPacket>> map = new HashMap<>();

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
        map.put(Opcode.UnAuthorization, UnAuthorization.class);
    }

    public static Class<? extends AbstractPacket> getClass(Opcode opcode) {
        return map.get(opcode);
    }
}
