package com.nicframework.netty.serializerpc.support.hessian;

import com.nicframework.netty.serializerpc.support.MessageCodecUtil;
import com.nicframework.netty.serializerpc.support.MessageDecoder;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 16:31
 */
public class HessianDecoder extends MessageDecoder
{
    public HessianDecoder(MessageCodecUtil util) {
        super(util);
    }
}
