package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.SearchUserReq)
public class SearchUserReq extends AbstractPacket {
    private String nickname;

    public SearchUserReq(String nickname) {
        this.nickname = nickname;
    }

    public SearchUserReq() {
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "SearchUserReq{" +
                "nickname='" + nickname + '\'' +
                '}';
    }

    @Override
    public void read(ByteBuf buf) {
        nickname = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(buf, nickname);
    }
}
