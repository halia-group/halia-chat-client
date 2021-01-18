package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.FriendApplyReq)
public class FriendApplyReq extends AbstractPacket {
    private int receiverId;
    private String reason;

    public FriendApplyReq(int receiverId, String reason) {
        this.receiverId = receiverId;
        this.reason = reason;
    }

    public FriendApplyReq() {
    }

    public int getReceiverId() {
        return receiverId;
    }

    @Override
    public String toString() {
        return "FriendApplyReq{" +
                "receiverId=" + receiverId +
                ", reason='" + reason + '\'' +
                '}';
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void read(ByteBuf buf) {
        receiverId = buf.readInt();
        reason = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(receiverId);
        writeString(buf, reason);
    }
}
