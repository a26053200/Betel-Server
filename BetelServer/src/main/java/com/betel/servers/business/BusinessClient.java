package com.betel.servers.business;

import com.betel.config.ServerConfigVo;
import com.betel.servers.business.BusinessMonitor;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

/**
 * @ClassName: BusinessClient
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/8/10 22:00
 */
public class BusinessClient
{
    final static Logger logger = Logger.getLogger(BusinessClient.class);
    private final String host;
    private final int port;
    private final String serverName;

    private BusinessMonitor monitor;

    private Channel channel;

    public boolean isDead = true;

    public BusinessClient(ServerConfigVo srvCfg, BusinessMonitor monitor)
    {
        this.host = srvCfg.getHost();
        this.port = srvCfg.getPort();
        this.monitor = monitor;
        this.serverName = srvCfg.getName();
    }

    public void run() throws Exception
    {
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            logger.info("核心业务服务器客户端开始启动...");
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast(new BusinessServerDecoder(monitor));
                            ch.pipeline().addLast(new BusinessServerEncoder(monitor));
                            ch.pipeline().addLast(new BusinessClientHandler(monitor, serverName));
                        }
                    });

            ChannelFuture f = b.connect(host, port).sync();
            channel = f.channel();
            logger.info("BusinessClient connect " + this.serverName + " successful!!!");
            monitor.SetGameServerClient(this);
            monitor.handshake(channel);
            f.channel().closeFuture().sync();
        }
        finally
        {
            group.shutdownGracefully();
            isDead = true;
            logger.info("BusinessClient disconnect from " + this.serverName);
        }
    }

    public Channel GetChanel()
    {
        return channel;
    }

    public static void start(final ServerConfigVo srvCfg, final BusinessMonitor monitor)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                logger.info("Business_Client连接服务器:" + srvCfg.getName());
                BusinessClient client = null;
                try
                {
                    client = new BusinessClient(srvCfg, monitor);
                    client.run();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, "Business_Client --> " + srvCfg.getName()).start();
    }
}
