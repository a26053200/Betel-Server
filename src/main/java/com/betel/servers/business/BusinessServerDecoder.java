package com.betel.servers.business;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @ClassName: GateClientDecoder
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/6/1 21:00
 */
public class BusinessServerDecoder extends LengthFieldBasedFrameDecoder
{
    final static Logger logger = Logger.getLogger(BusinessServerDecoder.class);

    BusinessMonitor monitor;

    public BusinessServerDecoder(BusinessMonitor monitor)
    {
        super(Integer.MAX_VALUE,0,4);
        this.monitor = monitor;
    }
//    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception
//    {
//        super.decode(ctx, msg, out);
//        //monitor.recvJsonBuff(ctx,msg);
//        monitor.recvByteBuf(ctx,msg);
//    }
}
