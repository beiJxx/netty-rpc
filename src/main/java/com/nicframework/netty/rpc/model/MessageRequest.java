package com.nicframework.netty.rpc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * rpc服务请求结构
 *
 * @author james
 * @date 2021/7/28 14:14
 */
@Data
public class MessageRequest implements Serializable
{
    private static final long serialVersionUID = -4876406167434646229L;

    private String messageId;
    private String className;
    private String methodName;
    private Class<?>[] typeParameters;
    private Object[] parameters;

}
