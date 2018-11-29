package com.betel.servers.balance;

import com.betel.common.BaseServer;
import com.betel.common.Debug;
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
 * @ClassName: BalanceServer
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/8 22:38
 */
public class BalanceServer extends BaseServer
{
    public static final String ServerName = "BalanceServer";

    public BalanceServer()
    {
        super(ServerName);
    }

    @Override
    public void run() throws Exception
    {
        Logger logger = Logger.getLogger(BalanceServer.class);
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        BalanceMonitor monitor = new BalanceMonitor();
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
                            ch.pipeline().addLast(new BalanceServerDecoder(monitor));
                            ch.pipeline().addLast(new BalanceServerEncoder(monitor));
                            ch.pipeline().addLast(new BalanceServerHandler(monitor));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)


            ChannelFuture f = b.bind(srvCfg.getPort()).sync(); // (7)
            logger.info(ServerName + " startup successful!!!");
            //网关客户端连接游戏服务器
            //GateClient.start(ServerConstant.ServerName.GAME_SERVER,"",8090,monitor);
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
        Debug.initLog("[" + ServerName + "]", "log4j_gate_server.properties");
        new BalanceServer().run();
    }
}
