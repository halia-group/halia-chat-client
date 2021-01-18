package com.ddhigh.halia.chat.client.io.protocol;

import com.ddhigh.halia.chat.client.io.packet.AbstractPacket;
import com.ddhigh.halia.chat.client.io.packet.PacketFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MessageCodec extends ByteToMessageCodec<AbstractPacket> {
    private static final Logger logger = LoggerFactory.getLogger(MessageCodec.class);
    public static final int MAGIC_NUMBER = 0xcafe;

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, ByteBuf out) throws Exception {
        var body = Unpooled.buffer();
        msg.write(body);
        // magicNumber
        out.writeShort(MAGIC_NUMBER);
        var packet = msg.getClass().getAnnotation(Packet.class);
        // opcode
        out.writeShort(packet.opcode().value());
        // body length
        out.writeShort(body.readableBytes());
        out.writeBytes(body);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(ByteBufUtil.prettyHexDump(in));
        if (in.readableBytes() < 6) { // 2字节magicNumber, 2字节opcode, 2字节长度
            return;
        }
        var magicNumber = in.readUnsignedShort();
        if (magicNumber != MAGIC_NUMBER) {
            logger.warn("invalid magicNumber {}", magicNumber);
            return;
        }
        var opcodeValue = in.readShort();
        var opcode = Opcode.valueOf(opcodeValue);
        if (opcode == null) {
            logger.warn("invalid opcode {}", opcodeValue);
            return;
        }
        var length = in.readShort();
        if (in.readableBytes() < length) {
            return;
        }
        var clazz = PacketFactory.getClass(opcode);
        var packet = clazz.getDeclaredConstructor().newInstance();
        packet.read(in);
        out.add(packet);
    }
}
