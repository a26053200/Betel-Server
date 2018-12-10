package com.betel.servers.center;

import com.betel.coder.LengthPackageDecoder;
import com.betel.common.Monitor;
import com.betel.common.ServerBase;
import com.betel.config.ServerConfigVo;
import com.betel.handler.ByteBufHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @ClassName: CenterServer
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/1 0:20
 */
public class CenterServer extends ServerBase
{
    final static Logger logger = LogManager.getLogger(CenterServer.class);

    public CenterServer(ServerConfigVo serverConfig, Monitor monitor)
    {
        super(serverConfig, monitor);
    }

    @Override
    public void run() throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerConfigVo cfg = getServerConfig();
        try
        {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast(new LengthPackageDecoder(cfg.getDecoderLengthFieldOffset(),cfg.getDecoderLengthFieldLength()));
                            ch.pipeline().addLast(new ByteArrayEncoder());
                            ch.pipeline().addLast(new ByteBufHandler(getMonitor()));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)


            ChannelFuture f = b.bind(cfg.getPort()).sync(); // (7)
            logger.info(cfg.getName() + " startup successful!!!");
            f.channel().closeFuture().sync();
            logger.info(cfg.getName() + " close up...");
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
