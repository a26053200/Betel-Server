package com.betel.asd;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.interfaces.ICommunication;
import com.betel.asd.interfaces.ICommunicationFilter;
import com.betel.common.Monitor;

/**
 * @ClassName: BaseCommunication
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2019/2/26 23:19
 */
public abstract class BaseCommunication<T> implements ICommunication<T>
{
    protected Monitor monitor;

    protected String gatewayName;

    protected String serverName;

    public BaseCommunication(Monitor monitor, String gatewayName)
    {
        this.monitor = monitor;
        this.gatewayName = gatewayName;
    }

    public BaseCommunication(Monitor monitor, String gatewayName, String serverName)
    {
        this.monitor = monitor;
        this.gatewayName = gatewayName;
        this.serverName = serverName;
    }

    @Override
    public String getChannelId()
    {
        return null;
    }

    @Override
    public void push(String action, JSONObject json)
    {

    }

    @Override
    public void pushAll(String action, JSONObject json, ICommunicationFilter<T> filter)
    {

    }
    @Override
    public void pushAll(String action, JSONObject json)
    {

    }

}
