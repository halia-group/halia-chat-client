package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.LoginResp)
public class LoginResp extends AbstractPacket {
    private int code;
    private String message;
    private int userId;
    private String nickname;

    public LoginResp(int code, String message, int userId, String nickname) {
        this.code = code;
        this.message = message;
        this.userId = userId;
        this.nickname = nickname;
    }

    public LoginResp() {
    }

    public int getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void read(ByteBuf buf) {
        code = buf.readByte();
        message = readString(buf);
        userId = buf.readInt();
        nickname = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(code);
        writeString(buf, message);
        buf.writeInt(userId);
        writeString(buf, nickname);
    }

    @Override
    public String toString() {
        return "LoginResp{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", userId=" + userId +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
