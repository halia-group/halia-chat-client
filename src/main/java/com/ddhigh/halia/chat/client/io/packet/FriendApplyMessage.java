package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.FriendApplyMessage)
public class FriendApplyMessage extends AbstractPacket {
    private int senderId;
    private String nickname;
    private String reason;

    public FriendApplyMessage(int senderId, String nickname, String reason) {
        this.senderId = senderId;
        this.nickname = nickname;
        this.reason = reason;
    }

    public FriendApplyMessage() {
    }

    public int getSenderId() {
        return senderId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "FriendApplyMessage{" +
                "senderId=" + senderId +
                ", nickname='" + nickname + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    @Override
    public void read(ByteBuf buf) {
        senderId = buf.readInt();
        nickname = readString(buf);
        reason = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(senderId);
        writeString(buf, nickname);
        writeString(buf, reason);
    }
}
