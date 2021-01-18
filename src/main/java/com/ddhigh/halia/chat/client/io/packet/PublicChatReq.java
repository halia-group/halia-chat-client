package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.MsgType;
import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.PublicChatReq)
public class PublicChatReq extends AbstractPacket {
    private MsgType msgType;
    private String message;

    public PublicChatReq(MsgType msgType, String message) {
        this.msgType = msgType;
        this.message = message;
    }

    public PublicChatReq() {
    }

    @Override
    public void read(ByteBuf buf) {
        msgType = MsgType.valueOf(buf.readByte());
        message = readShortString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(msgType.value());
        writeShortString(buf, message);
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "PublicChatReq{" +
                "msgType=" + msgType +
                ", message='" + message + '\'' +
                '}';
    }
}
