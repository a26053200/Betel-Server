package com.betel.config;

/**
 * @ClassName: ServerConfigVo
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/13 1:09
 */
public class ServerConfigVo
{
    private String name;
    private String host;
    private int port;
    private String dbHost;
    private int dbPort;

    public String getDbHost()
    {
        return dbHost;
    }

    public int getDbPort()
    {
        return dbPort;
    }

    public String getName()
    {
        return name;
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }

    public ServerConfigVo(String name, String host, int port, String dbHost, int dbPort)
    {
        this.name = name;
        this.host = host;
        this.port = port;
        this.dbHost = dbHost;
        this.dbPort = dbPort;
    }
}
