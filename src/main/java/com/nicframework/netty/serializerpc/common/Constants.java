package com.nicframework.netty.serializerpc.common;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/29 8:39
 */
public class Constants
{
    //ObjectDecoder 底层默认继承半包解码器LengthFieldBasedFrameDecoder处理粘包问题的时候，
    //消息头开始即为长度字段，占据4个字节。这里出于保持兼容的考虑
    public final static int MESSAGE_LENGTH = 4;

    public final static String DELIMITER = ":";
}
