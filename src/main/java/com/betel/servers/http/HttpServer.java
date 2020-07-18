package com.betel.servers.http;

import com.betel.common.Monitor;
import com.betel.common.ServerBase;
import com.betel.common.interfaces.IServerClient;
import com.betel.config.ServerConfigVo;
import com.betel.handler.HttpServerHandler;
import com.betel.servers.forward.ServerClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @ClassName: HttpServer
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/1 0:11
 */
public class HttpServer extends ServerBase implements IServerClient
{
    final static Logger logger = LogManager.getLogger(HttpServer.class);

    private ServerClient serverClient;

    public void setServerClient(ServerClient serverClient)
    {
        this.serverClient = serverClient;
    }

    public HttpServer(ServerConfigVo serverConfig, Monitor monitor)
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
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            ch.pipeline().addLast(new HttpObjectAggregator(65536));
                            ch.pipeline().addLast(new HttpServerHandler(getMonitor()));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            ChannelFuture f = b.bind(cfg.getPort()).sync(); // (7)
            logger.info(String.format("%s:%d startup successful!!!",cfg.getName(),cfg.getPort()));
            //服务器客户端连接服务器的服务器
            if (serverClient != null)
                start(cfg,getMonitor());

            f.channel().closeFuture().sync();
            logger.info(cfg.getName() + " close up...");
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public void start(ServerConfigVo srvCfg, Monitor monitor)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                logger.info("Http Server Client is connecting center server:" + srvCfg.getCenterServerName());
                try
                {
                    serverClient.run();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, "Http Server Client-->" + srvCfg.getCenterServerName()).start();
    }
}
