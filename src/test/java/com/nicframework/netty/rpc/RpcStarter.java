package com.nicframework.netty.rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:49
 */
public class RpcStarter
{
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:rpc-invoke-config.xml");
    }
}
