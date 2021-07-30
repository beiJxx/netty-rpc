package com.nicframework.netty.rpc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * rpc服务应答结构
 *
 * @author james
 * @date 2021/7/28 14:15
 */
@Data
public class MessageResponse implements Serializable
{

    private static final long serialVersionUID = 8955289153861118158L;
    private String messageId;
    private String error;
    private Object result;

}
