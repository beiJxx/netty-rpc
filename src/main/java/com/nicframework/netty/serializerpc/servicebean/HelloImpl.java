package com.nicframework.netty.serializerpc.servicebean;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 15:09
 */
public class HelloImpl implements IHello
{
    @Override
    public String getHello(String name) {
        return "hello " + name;
    }
}
