package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.RegisterReq)
public class RegisterReq extends AbstractPacket {
    private String username;
    private String password;
    private String nickname;

    public RegisterReq(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public RegisterReq() {
    }

    @Override
    public void read(ByteBuf buf) {
        username = readString(buf);
        password = readString(buf);
        nickname = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(buf, username);
        writeString(buf, password);
        writeString(buf, nickname);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "RegisterReq{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
