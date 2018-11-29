package com.betel.servers.balance;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.log4j.Logger;

import java.util.List;
/**
 * @ClassName: BalanceServerDecoder
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/9 0:27
 */
public class BalanceServerDecoder extends LengthFieldBasedFrameDecoder
{
    final static Logger logger = Logger.getLogger(BalanceServerDecoder.class);

    BalanceMonitor monitor;

    /**
     * -  maxFrameLength：设定包的最大长度，超出包的最大长度netty将会做一些特殊处理；
     * ﻿- lengthFieldOffset：指的是长度域的偏移量，表示跳过指定长度个字节之后的才是长度域LengthField；
     * - lengthFieldLength：记录该帧数据长度的字段本身的长度；
     * ﻿- initialBytesToStrip：从数据帧中跳过的字节数，表示获取完一个完整的数据包之后，忽略前面的指定的位数个字节，应用解码器拿到的就是不带长度域的数据包；
     */
    public BalanceServerDecoder(BalanceMonitor monitor)
    {
        super(Integer.MAX_VALUE,0,4);
        this.monitor = monitor;
    }
    //@Override
    //protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception
    //{
        //super.decode(ctx, msg, out);
        //monitor.recvJsonBuff(ctx,msg);

    //}
}
