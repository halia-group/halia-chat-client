package com.ddhigh.halia.chat.client.io.packet;

import com.ddhigh.halia.chat.client.io.protocol.Opcode;
import com.ddhigh.halia.chat.client.io.protocol.Packet;
import io.netty.buffer.ByteBuf;

@Packet(opcode = Opcode.FriendApplyResultResp)
public class FriendApplyResultResp extends AbstractPacket {
    private int code;
    private String message;

    public FriendApplyResultResp(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public FriendApplyResultResp() {
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "FriendApplyResultResp{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public void read(ByteBuf buf) {
        code = buf.readUnsignedByte();
        message = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(code);
        writeString(buf, message);
    }
}
