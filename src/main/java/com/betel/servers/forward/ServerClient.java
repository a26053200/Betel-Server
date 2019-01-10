package com.betel.servers.forward;

import com.betel.coder.LengthPackageDecoder;
import com.betel.common.Monitor;
import com.betel.config.ServerConfigVo;
import com.betel.handler.ByteBufHandler;
import com.betel.servers.center.CenterServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @ClassName: ServerClient
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/3 0:09
 */
public class ServerClient extends ServerClientBase
{
    final static Logger logger = LogManager.getLogger(CenterServer.class);

    public ServerClient(ServerConfigVo srvCfg, Monitor monitor)
    {
        super(srvCfg,monitor);
    }

    @Override
    public void run() throws Exception
    {
        EventLoopGroup group = new NioEventLoopGroup();
        ServerConfigVo cfg = getServerConfig();
        try
        {
            logger.info("Server Client boot start...");
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast(new LengthPackageDecoder(cfg.getDecoderLengthFieldOffset(),cfg.getDecoderLengthFieldLength()));
                            ch.pipeline().addLast(new ByteArrayEncoder());
                            ch.pipeline().addLast(new ByteBufHandler(getMonitor()));
                        }
                    });


            ChannelFuture f = b.connect(cfg.getCenterServerHost(), cfg.getCenterServerPort()).sync();
            channel = f.channel();
            logger.info("Client connect " + cfg.getCenterServerName() + " successful!!!");
            ForwardMonitor forwardMonitor = (ForwardMonitor)getMonitor();
            forwardMonitor.setServerClient(this);
            forwardMonitor.handshake(channel);
            f.channel().closeFuture().sync();
        }
        finally
        {
            group.shutdownGracefully();
            logger.info("Client disconnect from " + cfg.getName());
        }
    }
}
