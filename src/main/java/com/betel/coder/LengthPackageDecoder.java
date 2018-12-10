package com.betel.coder;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @ClassName: 长度包编码器
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/1 0:10
 */
public class LengthPackageDecoder extends LengthFieldBasedFrameDecoder
{

    public LengthPackageDecoder()
    {
        super(Integer.MAX_VALUE,0,4);
    }
    public LengthPackageDecoder(int lengthFieldOffset,int lengthFieldLength)
    {
        super(Integer.MAX_VALUE,lengthFieldOffset,lengthFieldLength);
    }
    /**
     * -  maxFrameLength：设定包的最大长度，超出包的最大长度netty将会做一些特殊处理；
     * ﻿- lengthFieldOffset：指的是长度域的偏移量，表示跳过指定长度个字节之后的才是长度域LengthField；
     * - lengthFieldLength：记录该帧数据长度的字段本身的长度；
     * ﻿- initialBytesToStrip：从数据帧中跳过的字节数，表示获取完一个完整的数据包之后，忽略前面的指定的位数个字节，应用解码器拿到的就是不带长度域的数据包；
     */
    public LengthPackageDecoder(int maxFrameLength,int lengthFieldOffset,int lengthFieldLength)
    {
        super(maxFrameLength,lengthFieldOffset,lengthFieldLength);
    }
}
