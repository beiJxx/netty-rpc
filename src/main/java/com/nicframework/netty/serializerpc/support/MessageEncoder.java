package com.nicframework.netty.serializerpc.support;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 16:23
 */
public class MessageEncoder extends MessageToByteEncoder<Object>
{

    private MessageCodecUtil util = null;

    public MessageEncoder(final MessageCodecUtil util) {
        this.util = util;
    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, final Object msg, final ByteBuf out) throws Exception {
        util.encode(out, msg);
    }
}
