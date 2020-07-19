package com.betel.handler;

import com.betel.common.Monitor;
import com.betel.utils.BytesUtils;
import com.betel.utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.*;
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

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); //Disk

    private HttpRequest request = null;

    private HttpPostRequestDecoder decoder;

    public HttpServerHandler(Monitor monitor)
    {
        this.monitor = monitor;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (decoder != null) {
            decoder.cleanFiles();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception
    {

        if (msg instanceof HttpRequest)
        {
            request = (HttpRequest) msg;
            if (request.method() == HttpMethod.GET)
            {
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
            }else if (request.method() == HttpMethod.POST)
            {
                try
                {
                    decoder = new HttpPostRequestDecoder(factory, request);
                }
                catch (Exception e)
                {//处理出错，返回错误信息
                    e.printStackTrace();
                    logger.error("Account SERVER Error");
                    responseError(ctx, "Account SERVER Error");
                }
            }
        }
        if (decoder != null)
        {
            if (msg instanceof HttpContent)
            {
                HttpContent chunk = (HttpContent) msg;
                decoder.offer(chunk);
                try
                {
                    for (InterfaceHttpData data : decoder.getBodyHttpDatas())
                    {
                        if(data.getName().equals("unityData"))
                        {
                            if(data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload)
                            {
                                FileUpload fileUpload = (FileUpload) data;
                                String fileName = fileUpload.getFilename();
                                if(!monitor.recvHttp(ctx, fileUpload.getByteBuf()))
                                {
                                    //logger.error("Http request data is Empty!");
                                    //responseError(ctx, "Http request data is Empty!");
                                }
                            }
                            break;
                        }
                    }
                }
                catch (Exception e)
                {//处理出错，返回错误信息
                    e.printStackTrace();
                    logger.error("Account SERVER Error");
                    responseError(ctx,"Account SERVER Error");
                }
                if (msg instanceof LastHttpContent)
                {
                    //logger.error("Last Http Content");
                    decoder.destroy();
                    decoder = null;
                }
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
        logger.error("http server exceptionCaught.. cause:" + cause.toString());
        ctx.close();
    }
}
