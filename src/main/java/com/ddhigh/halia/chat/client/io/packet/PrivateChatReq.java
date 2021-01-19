package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.MsgType;
import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.PrivateChatReq)
public class PrivateChatReq extends AbstractPacket {
    private int receiverId;
    private MsgType msgType;
    private String message;

    public PrivateChatReq(int receiverId, MsgType msgType, String message) {
        this.receiverId = receiverId;
        this.msgType = msgType;
        this.message = message;
    }

    public PrivateChatReq() {
    }

    public int getReceiverId() {
        return receiverId;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "PrivateChatReq{" +
                "receiverId=" + receiverId +
                ", msgType=" + msgType +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public void read(ByteBuf buf) {
        receiverId = buf.readInt();
        msgType = MsgType.valueOf(buf.readUnsignedByte());
        message = readShortString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(receiverId);
        buf.writeByte(msgType.value());
        writeShortString(buf, message);
    }
}
