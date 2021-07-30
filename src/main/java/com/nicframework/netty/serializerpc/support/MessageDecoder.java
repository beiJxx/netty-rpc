package com.nicframework.netty.serializerpc.support;

import com.nicframework.netty.serializerpc.common.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 16:23
 */
public class MessageDecoder extends ByteToMessageDecoder
{

    private MessageCodecUtil util = null;

    public MessageDecoder(final MessageCodecUtil util) {
        this.util = util;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        //出现粘包导致消息头长度不对，直接返回
        if (in.readableBytes() < Constants.MESSAGE_LENGTH) {
            return;
        }

        in.markReaderIndex();
        //读取消息的内容长度
        int messageLength = in.readInt();

        if (messageLength < 0) {
            ctx.close();
        }

        //读到的消息长度和报文头的已知长度不匹配。那就重置一下ByteBuf读索引的位置
        if (in.readableBytes() < messageLength) {
            in.resetReaderIndex();
            return;
        }
        else {
            byte[] messageBody = new byte[messageLength];
            in.readBytes(messageBody);

            try {
                Object obj = util.decode(messageBody);
                out.add(obj);
            }
            catch (IOException ex) {
                Logger.getLogger(MessageDecoder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
