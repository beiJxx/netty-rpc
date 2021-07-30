package com.nicframework.netty.serializerpc.servicebean;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:45
 */
public class CalculateImpl implements ICalculate
{
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
