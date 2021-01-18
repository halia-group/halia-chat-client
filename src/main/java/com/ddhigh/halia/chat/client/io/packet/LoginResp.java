package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.LoginResp)
public class LoginResp extends AbstractPacket {
    private int code;
    private String message;

    public LoginResp(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public LoginResp() {
    }

    @Override
    public void read(ByteBuf buf) {
        code = buf.readByte();
        message = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(code);
        writeString(buf, message);
    }

    @Override
    public String toString() {
        return "LoginResp{" +
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
