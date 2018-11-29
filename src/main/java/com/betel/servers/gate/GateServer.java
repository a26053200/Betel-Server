package com.betel.servers.gate;

import com.betel.common.BaseServer;
import com.betel.common.Debug;
import com.betel.config.ServerConfig;
import com.betel.config.ServerConfigVo;
import com.betel.consts.ServerName;
import com.betel.servers.balance.BalanceServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.apache.log4j.Logger;

/**
 * @ClassName: GateServer
 * @Description: 网关服务器
 * @Author: zhengnan
 * @Date: 2018/6/1 20:49
 */
public class GateServer extends BaseServer
{
    public static final String ServerName = "GateServer";

    public GateServer()
    {
        super(ServerName);
    }

    @Override
    public void run() throws Exception
    {
        Logger logger = Logger.getLogger(GateServer.class);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        GateMonitor monitor = new GateMonitor();
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast(new GateServerInboundHandler(monitor));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            logger.info(ServerName + " startup successful!!!");
            ChannelFuture f = b.bind(srvCfg.getPort()).sync();

            //网关客户端连接均衡服务器
            ServerConfigVo balanceServer = ServerConfig.getServerConfig(com.betel.consts.ServerName.BALANCE_SERVER);
            GateClient.start(balanceServer,monitor);

            //logger.info("Try to bootstrap balance server...");
            //BalanceServer bs = new BalanceServer(8081);
            //bs.run();
            //monitor.SetBalanceServer(bs);
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
        new GateServer().run();
    }
}
