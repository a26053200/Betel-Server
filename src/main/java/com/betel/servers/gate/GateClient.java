package com.betel.servers.gate;

import com.betel.config.ServerConfigVo;
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
 * @ClassName: GateClient
 * @Description: 网关客户端
 * @Author: zhengnan
 * @Date: 2018/6/5 23:24
 */
public class GateClient
{
    final static Logger logger = Logger.getLogger(GateClient.class);
    private final String host;
    private final int port;
    private final String serverName;

    private GateMonitor monitor;

    private Channel channel;

    public boolean isDead = true;

    public GateClient(ServerConfigVo srvCfg,GateMonitor monitor)
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
            logger.info("网关客户端开始启动...");
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast(new GateClientEncoder(monitor));
                            ch.pipeline().addLast(new GateClientDecoder(monitor));
                            ch.pipeline().addLast(new GateClientHandler(monitor, serverName));
                        }
                    });


            ChannelFuture f = b.connect(host, port).sync();
            channel = f.channel();
            logger.info("GateClient connect " + this.serverName + " successful!!!");
            monitor.SetGateServerClient(this);
            monitor.handshake(channel);
            f.channel().closeFuture().sync();
        }
        finally
        {
            group.shutdownGracefully();
            isDead = true;
            logger.info("GateClient disconnect from " + this.serverName);
        }
    }

    public Channel GetChanel()
    {
        return channel;
    }

    public static void start(ServerConfigVo srvCfg, final GateMonitor monitor)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                logger.info("Gate_Client连接服务器:" + srvCfg.getName());
                GateClient client = null;
                try
                {
                    client = new GateClient( srvCfg, monitor);
                    client.run();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, "Gate_Client-->" + srvCfg.getName()).start();
    }
}
