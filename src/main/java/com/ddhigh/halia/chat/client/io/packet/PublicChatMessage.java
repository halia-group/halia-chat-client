package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.MsgType;
import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

import java.time.Instant;

@Packet(opcode = Opcode.PublicChatMessage)
public class PublicChatMessage extends AbstractPacket {
    private Instant time;
    private String publisher;
    private MsgType msgType;
    private String message;

    public PublicChatMessage(Instant time, String publisher, MsgType msgType, String message) {
        this.time = time;
        this.publisher = publisher;
        this.msgType = msgType;
        this.message = message;
    }

    public PublicChatMessage() {
    }

    @Override
    public void read(ByteBuf buf) {
        time = Instant.ofEpochSecond(buf.readLong());
        publisher = readString(buf);
        msgType = MsgType.valueOf(buf.readByte());
        message = readShortString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(time.getEpochSecond());
        writeString(buf, publisher);
        buf.writeByte(msgType.value());
        writeShortString(buf, message);
    }

    @Override
    public String toString() {
        return "PublicChatMessage{" +
                "time=" + time +
                ", publisher='" + publisher + '\'' +
                ", msgType=" + msgType +
                ", message='" + message + '\'' +
                '}';
    }

    public Instant getTime() {
        return time;
    }

    public String getPublisher() {
        return publisher;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public String getMessage() {
        return message;
    }
}
