package com.betel.servers.balance;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @ClassName: BalanceServerEncoder
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/9 0:28
 */
public class BalanceServerEncoder extends ByteArrayEncoder
{
    final static Logger logger = Logger.getLogger(BalanceServerEncoder.class);

    BalanceMonitor monitor;

    public BalanceServerEncoder(BalanceMonitor monitor)
    {
        this.monitor = monitor;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception
    {
        logger.info("send msg:"+msg.length);
        super.encode(ctx,msg,out);
    }
}
