package com.ddhigh.halia.chat.client.io.packet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public abstract class AbstractPacket {
    public abstract void read(ByteBuf buf);

    public abstract void write(ByteBuf buf);

    protected String readString(ByteBuf buf) {
        var length = buf.readUnsignedByte();
        var bytes = new byte[length];
        buf.readBytes(bytes);
        System.out.printf("%d bytes", bytes.length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    protected String readShortString(ByteBuf buf) {
        var length = buf.readUnsignedShort();
        var bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    protected void writeString(ByteBuf buf, String s) {
        var bytes = s.getBytes(StandardCharsets.UTF_8);
        buf.writeByte(bytes.length);
        buf.writeBytes(bytes);
    }

    protected void writeShortString(ByteBuf buf, String s) {
        var bytes = s.getBytes(StandardCharsets.UTF_8);
        buf.writeShort(bytes.length);
        buf.writeBytes(bytes);
    }
}
