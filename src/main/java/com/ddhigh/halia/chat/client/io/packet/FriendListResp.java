package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.FriendStatus;
import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

@Packet(opcode = Opcode.FriendListResp)
public class FriendListResp extends AbstractPacket {
    private int code;
    private String message;
    private List<Friend> list = new ArrayList<>();

    public FriendListResp(int code, String message, List<Friend> list) {
        this.code = code;
        this.message = message;
        this.list = list;
    }

    public FriendListResp() {
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Friend> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "FriendListResp{" +
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
            int id = buf.readInt();
            String nickname = readString(buf);
            int status = buf.readUnsignedByte();
            list.add(new Friend(id, nickname, FriendStatus.valueOf(status)));
        }
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(code);
        writeString(buf, message);
        buf.writeByte(list.size());
        for (Friend friend : list) {
            buf.writeInt(friend.id);
            writeString(buf, friend.nickname);
            buf.writeByte(friend.status.value());
        }
    }

    public static class Friend {
        private final int id;
        private final String nickname;
        private final FriendStatus status;

        public Friend(int id, String nickname, FriendStatus status) {
            this.id = id;
            this.nickname = nickname;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public String getNickname() {
            return nickname;
        }

        public FriendStatus getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return "Friend{" +
                    "id=" + id +
                    ", nickname='" + nickname + '\'' +
                    ", status=" + status +
                    '}';
        }
    }
}
