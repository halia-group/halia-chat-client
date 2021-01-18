package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

@Packet(opcode = Opcode.SearchUserResp)
public class SearchUserResp extends AbstractPacket {
    private int code;
    private String message;
    private List<User> list = new ArrayList<>();

    public SearchUserResp(int code, String message, List<User> list) {
        this.code = code;
        this.message = message;
        this.list = list;
    }

    public SearchUserResp() {
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<User> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "SearchUserResp{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", list=" + list +
                '}';
    }

    @Override
    public void read(ByteBuf buf) {
        code = buf.readUnsignedByte();
        message = readString(buf);
        var count = buf.readUnsignedByte();
        for (int i = 0; i < count; i++) {
            var user = new User(buf.readInt(), readString(buf));
            list.add(user);
        }
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(code);
        writeString(buf, message);
        buf.writeByte(list.size());
        for (User user : list) {
            buf.writeInt(user.id);
            writeString(buf, user.nickname);
        }
    }

    public static class User {
        private final int id;
        private final String nickname;

        public User(int id, String nickname) {
            this.id = id;
            this.nickname = nickname;
        }

        public int getId() {
            return id;
        }

        public String getNickname() {
            return nickname;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", nickname='" + nickname + '\'' +
                    '}';
        }
    }
}
