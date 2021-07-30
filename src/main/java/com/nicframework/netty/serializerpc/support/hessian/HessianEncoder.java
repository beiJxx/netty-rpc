package com.nicframework.netty.serializerpc.support.hessian;

import com.nicframework.netty.serializerpc.support.MessageCodecUtil;
import com.nicframework.netty.serializerpc.support.MessageEncoder;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 16:31
 */
public class HessianEncoder extends MessageEncoder
{
    public HessianEncoder(MessageCodecUtil util) {
        super(util);
    }
}
