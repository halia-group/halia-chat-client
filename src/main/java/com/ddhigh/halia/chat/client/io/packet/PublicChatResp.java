package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.PublicChatResp)
public class PublicChatResp extends AbstractPacket {
    private int code;
    private String message;

    public PublicChatResp(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public PublicChatResp() {
    }

    @Override
    public void read(ByteBuf buf) {
        code = buf.readByte();
        message = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(code);
        writeString(buf,message);
    }

    @Override
    public String toString() {
        return "PublicChatResp{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
