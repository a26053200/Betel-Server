package com.betel.servers.forward;

import com.betel.common.Monitor;
import com.betel.common.ServerBase;
import com.betel.config.ServerConfigVo;
import io.netty.channel.Channel;

/**
 * @ClassName: ServerClientBase
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/1 0:57
 */
public abstract class ServerClientBase extends ServerBase
{
    protected Channel channel;

    public Channel getChannel()
    {
        return channel;
    }

    public ServerClientBase(ServerConfigVo srvCfg, Monitor monitor)
    {
        super(srvCfg,monitor);
    }
}
