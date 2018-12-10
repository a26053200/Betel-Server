package com.betel.servers.forward;

import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: ForwardContext 转发服务器保存的上下文
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/10 23:05
 */
public class ForwardContext
{
    private ChannelHandlerContext channelHandlerContext;
    private String clientType;

    public ChannelHandlerContext getChannelHandlerContext()
    {
        return channelHandlerContext;
    }

    public String getClientType()
    {
        return clientType;
    }

    public ForwardContext(ChannelHandlerContext channelHandlerContext, String clientType)
    {
        this.channelHandlerContext = channelHandlerContext;
        this.clientType = clientType;
    }
}
