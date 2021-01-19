package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.MsgType;
import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

import java.time.Instant;

@Packet(opcode = Opcode.PrivateChatMessage)
public class PrivateChatMessage extends AbstractPacket {
    private Instant time;
    private int senderId;
    private int receiverId;
    private String nickname;
    private MsgType msgType;
    private String message;

    public PrivateChatMessage(Instant time, int senderId, int receiverId, String nickname, MsgType msgType, String message) {
        this.time = time;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.nickname = nickname;
        this.msgType = msgType;
        this.message = message;
    }

    public PrivateChatMessage() {
    }

    public Instant getTime() {
        return time;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getNickname() {
        return nickname;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "PrivateChatMessage{" +
                "time=" + time +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", nickname='" + nickname + '\'' +
                ", msgType=" + msgType +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public void read(ByteBuf buf) {
        time = Instant.ofEpochSecond(buf.readLong());
        senderId = buf.readInt();
        receiverId = buf.readInt();
        nickname = readString(buf);
        msgType = MsgType.valueOf(buf.readUnsignedByte());
        message = readShortString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(time.getEpochSecond());
        buf.writeInt(senderId);
        buf.writeInt(receiverId);
        writeString(buf, nickname);
        buf.writeByte(msgType.value());
        writeShortString(buf, message);
    }
}
