package com.nicframework.netty.serializerpc.support.kryo;

import com.nicframework.netty.serializerpc.support.MessageCodecUtil;
import com.nicframework.netty.serializerpc.support.MessageDecoder;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/27 9:28
 */
public class KryoDecoder extends MessageDecoder
{
    public KryoDecoder(MessageCodecUtil util) {
        super(util);
    }
}
