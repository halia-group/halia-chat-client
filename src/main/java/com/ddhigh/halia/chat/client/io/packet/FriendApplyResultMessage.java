package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.FriendStatus;
import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.FriendApplyResultMessage)
public class FriendApplyResultMessage extends AbstractPacket {
    private int receiverId;
    private String nickname;
    private FriendStatus status;
    private String reason;

    public FriendApplyResultMessage(int receiverId, String nickname, FriendStatus status, String reason) {
        this.receiverId = receiverId;
        this.nickname = nickname;
        this.status = status;
        this.reason = reason;
    }

    public FriendApplyResultMessage() {
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getNickname() {
        return nickname;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "FriendApplyResultMessage{" +
                "receiverId=" + receiverId +
                ", nickname='" + nickname + '\'' +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }

    @Override
    public void read(ByteBuf buf) {
        receiverId = buf.readInt();
        nickname = readString(buf);
        status = FriendStatus.valueOf(buf.readUnsignedByte());
        reason = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(receiverId);
        writeString(buf, nickname);
        buf.writeByte(status.value());
        writeString(buf, reason);
    }
}
