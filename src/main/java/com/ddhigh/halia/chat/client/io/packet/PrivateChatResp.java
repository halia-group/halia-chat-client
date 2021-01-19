package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.PrivateChatResp)
public class PrivateChatResp extends AbstractPacket {
    private int code;
    private String message;
    private int receiverId;

    public PrivateChatResp(int code, String message, int receiverId) {
        this.code = code;
        this.message = message;
        this.receiverId = receiverId;
    }

    public PrivateChatResp() {
    }

    public int getReceiverId() {
        return receiverId;
    }

    @Override
    public void read(ByteBuf buf) {
        code = buf.readByte();
        message = readString(buf);
        receiverId = buf.readInt();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(code);
        writeString(buf,message);
        buf.writeInt(receiverId);
    }

    @Override
    public String toString() {
        return "PrivateChatResp{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", receiverId=" + receiverId +
                '}';
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
