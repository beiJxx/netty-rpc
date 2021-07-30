package com.nicframework.netty.serializerpc.support;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 16:22
 */
public interface MessageCodecUtil
{
    void encode(final ByteBuf out, final Object message) throws IOException;

    Object decode(byte[] body) throws IOException;
}
