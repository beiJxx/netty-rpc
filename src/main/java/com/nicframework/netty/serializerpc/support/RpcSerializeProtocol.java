package com.nicframework.netty.serializerpc.support;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Description:
 * RPC消息序序列化协议类型
 * 由于NettyRPC中的协议类型，目前已经支持Kryo序列化、Hessian序列化、Java原生本地序列化方式。
 * 考虑到可扩展性，故要抽象出RPC消息序列化，协议类型对象
 *
 * @author james
 * @date 2021/7/27 8:39
 */
public enum RpcSerializeProtocol
{

    //Java原生本地序列化方式
    JDKSERIALIZE("jdknative"),
    //Kryo序列化
    KRYOSERIALIZE("kryo"),
    //Hessian序列化
    HESSIANSERIALIZE("hessian");

    private String serializeProtocol;

    RpcSerializeProtocol(String serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    @Override
    public String toString() {
        ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toString(this);
    }

    public String getSerializeProtocol() {
        return serializeProtocol;
    }
}
