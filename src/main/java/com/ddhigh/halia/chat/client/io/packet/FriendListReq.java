package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.FriendListReq)
public class FriendListReq extends AbstractPacket {
    @Override
    public void read(ByteBuf buf) {

    }

    @Override
    public void write(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return "FriendListReq{}";
    }
}
