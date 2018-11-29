package com.betel.servers.business;

import com.betel.common.Monitor;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.log4j.Logger;

/**
 * @ClassName: BusinessClientHandler
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/8/10 22:01
 */
public class BusinessClientHandler extends SimpleChannelInboundHandler<ByteBuf>
{
    private static final Logger logger = Logger.getLogger(BusinessClientHandler.class);

    private BusinessMonitor monitor;

    private String serverName;

    /**
     * Creates a client-side handler.
     */
    public BusinessClientHandler(BusinessMonitor monitor, String serverName)
    {
        this.monitor = monitor;
        this.serverName = serverName;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception
    {
        Channel incoming = ctx.channel();
        //logger.info("Game Client Channel:" + incoming.id());
        logger.info("收到数据长度:" + buf.readableBytes());
        monitor.recvByteBuf(ctx,buf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        super.channelReadComplete(ctx);
        Channel incoming = ctx.channel();
        logger.info("Game Client send to server ip:" + incoming.remoteAddress() + " msg over");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        Channel incoming = ctx.channel();
        logger.error("Game Client server ip:" + incoming.remoteAddress() + " is exception");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close().addListener(new GenericFutureListener<Future<? super Void>>()
        {
            @Override
            public void operationComplete(Future<? super Void> future)
                    throws Exception
            {
                if (future.isSuccess())
                    logger.info("[BusinessClient] 到 [GateServer]的连接已经断开");
            }
        });
    }
}
