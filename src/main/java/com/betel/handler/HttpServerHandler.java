package com.betel.handler;

import com.betel.common.Monitor;
import com.betel.utils.BytesUtils;
import com.betel.utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.logging.log4j.LogManager;

/**
 * @ClassName: HttpServerHandler
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/1 1:46
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter
{
    final static org.apache.logging.log4j.Logger logger = LogManager.getLogger(HttpServerHandler.class);

    private Monitor monitor;

    private HttpRequest request = null;

    public HttpServerHandler(Monitor monitor)
    {
        this.monitor = monitor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception
    {

        if (msg instanceof HttpRequest)
        {
            request = (HttpRequest) msg;
            String uri = request.uri();
            try
            {
                String res = uri.substring(1);
                if(!monitor.recvHttp(ctx, res))
                {
                    logger.error("Http request data is Empty!");
                    responseError(ctx, "Http request data is Empty!");
                }
            }
            catch (Exception e)
            {//处理出错，返回错误信息
                e.printStackTrace();
                logger.error("Account SERVER Error");
                responseError(ctx, "Account SERVER Error");
            }
        }
        if (msg instanceof HttpContent)
        {
            try
            {
                HttpContent content = (HttpContent) msg;
                if(!monitor.recvHttp(ctx, content.content()))
                {
                    logger.error("Http request data is Empty!");
                    responseError(ctx, "Http request data is Empty!");
                }
            }
            catch (Exception e)
            {//处理出错，返回错误信息
                e.printStackTrace();
                logger.error("Account SERVER Error");
                responseError(ctx,"Account SERVER Error");
            }
        }
    }

    private void responseError(ChannelHandlerContext ctx, String errorMsg)
    {
        //logger.error(errorMsg);
        ctx.channel().write(BytesUtils.string2Bytes(errorMsg));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        logger.info("Gate Http server channelReadComplete..");
        ctx.flush();//刷新后才将数据发出到SocketChannel
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception
    {
        logger.error("http server exceptionCaught..");
        ctx.close();
    }
}
