package com.vincent.netty.serializable.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        MessagePack messagePack = new MessagePack();
        int i = byteBuf.readableBytes();
        byte[] bytes = new byte[i];
        byteBuf.getBytes(byteBuf.readerIndex(), bytes, 0, i);
        list.add(messagePack.read(bytes, User.class));
    }
}
