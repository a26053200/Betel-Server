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
    private int port;
    private String centerServerName;
    private String centerServerHost;
    private int centerServerPort;
    private String dbHost;
    private int dbPort;
    private int dbIndex;
    private int decoderLengthFieldOffset;
    private int decoderLengthFieldLength;

    public String getName()
    {
        return name;
    }

    public int getPort()
    {
        return port;
    }

    public String getCenterServerName()
    {
        return centerServerName;
    }

    public String getCenterServerHost()
    {
        return centerServerHost;
    }

    public int getCenterServerPort()
    {
        return centerServerPort;
    }

    public String getDbHost()
    {
        return dbHost;
    }

    public int getDbPort()
    {
        return dbPort;
    }

    public int getDbIndex()
    {
        return dbIndex;
    }

    public int getDecoderLengthFieldOffset()
    {
        return decoderLengthFieldOffset;
    }

    public int getDecoderLengthFieldLength()
    {
        return decoderLengthFieldLength;
    }

    public ServerConfigVo(String name, int port, String centerServerName, String centerServerHost, int centerServerPort, String dbHost, int dbPort, int dbIndex, int decoderLengthFieldOffset, int decoderLengthFieldLength)
    {
        this.name = name;
        this.port = port;
        this.centerServerName = centerServerName;
        this.centerServerHost = centerServerHost;
        this.centerServerPort = centerServerPort;
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbIndex = dbIndex;
        this.decoderLengthFieldOffset = decoderLengthFieldOffset;
        this.decoderLengthFieldLength = decoderLengthFieldLength;
    }
}
