package com.betel.servers.gate;

import com.betel.common.Monitor;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.log4j.Logger;

/**
 * @ClassName: GateClientHandler
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/6/5 23:24
 */
public class GateClientHandler extends SimpleChannelInboundHandler<ByteBuf>
{
    private static final Logger logger = Logger.getLogger(GateClientHandler.class);

    private GateMonitor monitor;

    private String serverName;

    /**
     * Creates a client-side handler.
     */
    public GateClientHandler(GateMonitor monitor, String serverName)
    {
        this.monitor = monitor;
        this.serverName = serverName;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception
    {
        Channel incoming = ctx.channel();
        //logger.info("Gate Client Channel:" + incoming.id());
        logger.info("收到数据长度:" + buf.readableBytes());
        monitor.recvByteBuf(ctx,buf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        super.channelReadComplete(ctx);
        Channel incoming = ctx.channel();
        logger.info("Gate Client send to server ip:" + incoming.remoteAddress() + " msg over");
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        Channel incoming = ctx.channel();
        logger.error("Gate Client server ip:" + incoming.remoteAddress() + " is exception");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close().addListener(new GenericFutureListener<Future<? super Void>>()
        {
            @Override
            public void operationComplete(Future<? super Void> future)
                    throws Exception
            {
                if (future.isSuccess())
                    logger.info("[GateClient] 到 []GameServer]的连接已经断开");
            }
        });
    }
}
