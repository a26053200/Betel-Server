package com.betel.servers.business;

import com.betel.common.BaseServer;
import com.betel.common.Debug;
import com.betel.config.ServerConfig;
import com.betel.config.ServerConfigVo;
import com.betel.consts.ServerName;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

/**
 * @ClassName: BusinessServer
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/6/8 23:47
 */
public class BusinessServer extends BaseServer
{
    public static final String ServerName = "BusinessServer";

    public BusinessServer()
    {
        super(ServerName);
    }

    @Override
    public void run() throws Exception
    {
        Logger logger = Logger.getLogger(BusinessServer.class);
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        BusinessMonitor monitor = new BusinessMonitor();
        try
        {
            logger.info("核心业务服务器开始启动...");
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast(new BusinessServerDecoder(monitor));
                            ch.pipeline().addLast(new BusinessServerEncoder(monitor));
                            ch.pipeline().addLast(new BusinessServerHandler(monitor));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            ChannelFuture f = b.bind(srvCfg.getPort()).sync(); // (7)
            logger.info(ServerName + " startup successful!!!");
            //业务服务器连接LoadBalanceServer
            ServerConfigVo balanceServer = ServerConfig.getServerConfig(com.betel.consts.ServerName.BALANCE_SERVER);
            BusinessClient.start(balanceServer,monitor);
            f.channel().closeFuture().sync();
            logger.info(ServerName + " close up...");
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception
    {
        Debug.initLog("[" + ServerName + "]","log4j_business_server.properties");
        new BusinessServer().run();
    }
}
