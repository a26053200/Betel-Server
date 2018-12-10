package com.betel.common;

import com.betel.config.ServerConfigVo;
import com.betel.utils.IdGenerator;

/**
 * @ClassName: ServerBase
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/1 0:32
 */
public abstract class ServerBase
{
    private ServerConfigVo serverConfig;

    private Monitor monitor;

    public Monitor getMonitor()
    {
        return monitor;
    }

    protected ServerConfigVo getServerConfig()
    {
        return serverConfig;
    }

    public ServerBase(ServerConfigVo serverConfig, Monitor monitor)
    {
        this.serverConfig = serverConfig;
        this.monitor = monitor;

        //Id生成器
        IdGenerator.init(Thread.currentThread().getId());
    }



    public abstract void run() throws Exception;
}
