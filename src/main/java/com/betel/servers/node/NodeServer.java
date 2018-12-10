package com.betel.servers.node;

import com.betel.coder.LengthPackageDecoder;
import com.betel.common.Monitor;
import com.betel.common.ServerBase;
import com.betel.common.interfaces.IServerClient;
import com.betel.config.ServerConfigVo;
import com.betel.handler.ByteBufHandler;
import com.betel.servers.forward.ServerClient;
import com.betel.servers.http.HttpServer;
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
 * @ClassName: NodeServer
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/6 0:26
 */
public class NodeServer extends ServerBase implements IServerClient
{
    final static Logger logger = LogManager.getLogger(HttpServer.class);

    private ServerConfigVo centerServerCfg;

    private ServerClient serverClient;

    public void setCenterServerCfg(ServerConfigVo centerServerCfg)
    {
        this.centerServerCfg = centerServerCfg;
    }

    public void setServerClient(ServerClient serverClient)
    {
        this.serverClient = serverClient;
    }

    public NodeServer(ServerConfigVo serverConfig, Monitor monitor)
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

            //服务器客户端连接服务器的服务器
            if (serverClient != null && centerServerCfg != null)
                start(centerServerCfg, getMonitor());

            f.channel().closeFuture().sync();
            logger.info(cfg + " close up...");
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
                logger.info("Node Server Client连接服务器:" + srvCfg.getName());
                try
                {
                    serverClient.run();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, "Node Server Client-->" + srvCfg.getName()).start();
    }
}
