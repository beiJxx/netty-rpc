package com.nicframework.netty.serializerpc.support.kryo;

import com.esotericsoftware.kryo.pool.KryoPool;
import com.google.common.io.Closer;
import com.nicframework.netty.serializerpc.support.MessageCodecUtil;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Description:
 * Kryo编解码工具类
 *
 * @author james
 * @date 2021/7/27 9:21
 */
public class KryoCodecUtil implements MessageCodecUtil
{
    private KryoPool pool;

    private static Closer closer = Closer.create();

    public KryoCodecUtil(KryoPool pool) {
        this.pool = pool;
    }

    @Override
    public void encode(ByteBuf out, Object message) throws IOException {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closer.register(byteArrayOutputStream);
            KryoSerialize kryoSerialize = new KryoSerialize(pool);
            kryoSerialize.serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            int length = body.length;
            out.writeInt(length);
            out.writeBytes(body);
        }
        finally {
            closer.close();
        }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            closer.register(byteArrayInputStream);
            KryoSerialize kryoSerialize = new KryoSerialize(pool);
            return kryoSerialize.deserialize(byteArrayInputStream);
        }
        finally {
            closer.close();
        }
    }
}
