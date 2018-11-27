package com.betel.servers.balance;

import com.alibaba.fastjson.JSONObject;
import com.betel.common.Monitor;
import com.betel.consts.Action;
import com.betel.consts.FieldName;
import com.betel.consts.ServerName;
import com.betel.utils.BytesUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * @ClassName: BalanceMonitor
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/8 22:47
 */
public class BalanceMonitor extends Monitor
{
    final static Logger logger = Logger.getLogger(BalanceMonitor.class);

    @Override
    protected void initDB()
    {
    }//负载均衡目前不需要数据库

    // 网关服务器上下文
    private ChannelHandlerContext gateServerContext;
    // 业务服务器上下文
    private ChannelHandlerContext businessServerContext;
    // 网关服务器上下文
    public ChannelHandlerContext getGateServerContext()
    {
        return gateServerContext;
    }
    // 业务服务器上下文
    public ChannelHandlerContext getBusinessServerContext()
    {
        return businessServerContext;
    }
    @Override
    protected void RespondJson(ChannelHandlerContext ctx, JSONObject jsonObject)
    {
        String server = jsonObject.get(FieldName.SERVER).toString();
        switch (server)
        {
            // 核心业务服务器
            case ServerName.BUSINESS_SERVER:
                forward2BusinessServer(jsonObject);
                break;
            // 网关服务器
            case ServerName.GATE_SERVER:
                //jsonObject.put(FieldName.SERVER,ServerName.CLIENT_MP);
                forward2GateServer(jsonObject);
                break;
            //均衡服务器的消息直接处理
            case ServerName.BALANCE_SERVER:
            {
                String action = jsonObject.getString(Action.NAME);
                switch (action)
                {
                    case Action.HANDSHAKE_GATE2BALANCE://网关服务器和均衡服务器握手成功
                        gateServerContext = ctx;
                        logger.info("The gate server and balance server handshake successfully!");
                        break;
                    case Action.HANDSHAKE_BUSINESS2BALANCE://业务服务器和均衡服务器握手成功
                        businessServerContext = ctx;
                        logger.info("The business server and balance server handshake successfully!");
                        break;
                }
            }
            break;
        }
    }

    // 转发给
    private void forward2GateServer(JSONObject jsonObject)
    {
        byte[] bytes = BytesUtils.string2Bytes(jsonObject.toString());
        sendBytes(gateServerContext.channel(),bytes);
    }

    // 转发给
    private void forward2BusinessServer(JSONObject jsonObject)
    {
        byte[] bytes = BytesUtils.string2Bytes(jsonObject.toString());
        sendBytes(businessServerContext.channel(),bytes);
    }
}
