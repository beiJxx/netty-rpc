package com.nicframework.netty.serializerpc.support.kryo;

import com.nicframework.netty.serializerpc.support.MessageCodecUtil;
import com.nicframework.netty.serializerpc.support.MessageEncoder;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/27 9:28
 */
public class KryoEncoder extends MessageEncoder
{
    public KryoEncoder(MessageCodecUtil util) {
        super(util);
    }
}
