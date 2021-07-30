package com.nicframework.netty.serializerpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:49
 */
public class RpcKryoStarter
{
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:serialize/rpc-invoke-config-kryo.xml");
    }
}
