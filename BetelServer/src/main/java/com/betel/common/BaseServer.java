package com.betel.common;

import com.betel.config.ServerConfigVo;
import com.betel.config.ServerConfig;
import com.betel.database.RedisClient;
import com.betel.utils.IdGenerator;
import com.betel.utils.NetUtils;
import org.apache.log4j.Logger;

/**
 * @ClassName: BaseServer
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/8 22:39
 */
public abstract class BaseServer
{
    private Logger logger = Logger.getLogger(BaseServer.class);

    protected ServerConfigVo srvCfg;

    public BaseServer(String serverName)
    {
        srvCfg = ServerConfig.getServerConfig(serverName);
        logger.info("[" + serverName + "] start " + srvCfg.getHost() + ":" + srvCfg.getPort());
        //连接数据库
        RedisClient.getInstance().connectDB(srvCfg.getDbHost(),srvCfg.getDbPort());
        //Id生成器
        IdGenerator.init(Thread.currentThread().getId());
    }

    public abstract void run() throws Exception;
}
