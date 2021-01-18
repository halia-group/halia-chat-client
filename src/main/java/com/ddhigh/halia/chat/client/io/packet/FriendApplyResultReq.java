package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.FriendStatus;
import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.FriendApplyResultReq)
public class FriendApplyResultReq extends AbstractPacket {
    private int senderId;
    private FriendStatus status;
    private String reason;

    public FriendApplyResultReq(int senderId, FriendStatus status, String reason) {
        this.senderId = senderId;
        this.status = status;
        this.reason = reason;
    }

    public FriendApplyResultReq() {
    }

    public int getSenderId() {
        return senderId;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "FriendApplyResultReq{" +
                "senderId=" + senderId +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }

    @Override
    public void read(ByteBuf buf) {
        senderId = buf.readInt();
        status = FriendStatus.valueOf(buf.readUnsignedByte());
        reason = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(senderId);
        buf.writeByte(status.value());
        writeString(buf, reason);
    }
}
