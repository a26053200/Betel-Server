package com.betel.servers.center;

import com.alibaba.fastjson.JSONObject;
import com.betel.common.Monitor;
import com.betel.config.ServerConfigVo;
import com.betel.consts.Action;
import com.betel.consts.FieldName;
import com.betel.utils.BytesUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * @ClassName: CenterMonitor
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/1 1:20
 */
public class CenterMonitor extends Monitor
{
    final static Logger logger = LogManager.getLogger(CenterServer.class);

    private HashMap<String, ChannelHandlerContext> forwardContextMap;

    public CenterMonitor(ServerConfigVo serverCfgInfo)
    {
        super(serverCfgInfo);
        forwardContextMap = new HashMap<>();
    }

    @Override
    protected void RespondJson(ChannelHandlerContext ctx, JSONObject jsonObject)
    {
        String server = jsonObject.get(FieldName.SERVER).toString();
        if (server.equals(getServerName()))
        {//中心服务器直接处理
            String action = jsonObject.getString(FieldName.ACTION);
            switch (action)
            {
                case Action.HANDSHAKE://服务器和服务器握手成功
                    String handshakeServer = jsonObject.getString(FieldName.HANDSHAKE_SERVER);
                    forwardContextMap.put(handshakeServer, ctx);
                    logger.info("The " + handshakeServer + " and center server handshake successfully!");
                    break;
                default:
                    break;
            }
        }
        else
        {//转发给其他服务器处理
            ChannelHandlerContext forwardContext = forwardContextMap.get(server);
            if (forwardContext == null)
                logger.error("The " + server + " has not register in center server!");
            else
                forward2Server(forwardContext, jsonObject);
        }
    }

    @Override
    public void sendToServer(String serverName, String action, JSONObject data)
    {

    }

    @Override
    public void pushToClient(String channelId, String serverName, String action, JSONObject data)
    {

    }

    // 转发给
    private void forward2Server(ChannelHandlerContext ctx, JSONObject jsonObject)
    {
        byte[] bytes = BytesUtils.string2Bytes(jsonObject.toString());
        sendBytes(ctx.channel(), bytes);
    }


}
