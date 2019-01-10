package com.betel.servers.http;

import com.alibaba.fastjson.JSONObject;
import com.betel.config.ServerConfigVo;
import com.betel.consts.ClientType;
import com.betel.consts.FieldName;
import com.betel.servers.center.CenterServer;
import com.betel.servers.forward.ForwardContext;
import com.betel.servers.forward.ForwardMonitor;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @ClassName: HttpServerMonitor
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/3 0:52
 */
public class HttpServerMonitor extends ForwardMonitor
{
    final static Logger logger = LogManager.getLogger(CenterServer.class);

    public HttpServerMonitor(ServerConfigVo serverCfgInfo)
    {
        super(serverCfgInfo);
    }

    @Override
    protected void forward2Client(JSONObject jsonObject)
    {
        String channelId = jsonObject.getString(FieldName.CHANNEL_ID);
        removeIdentityInfo(jsonObject);
        ForwardContext clientCtx = getContext(channelId);
        if (clientCtx != null)
        {//当前服务器直接处理,然后返回给客户端
            String clientType = clientCtx.getClientType();
            httpRspd(clientCtx.getChannelHandlerContext().channel(),jsonObject.toString(),clientType.equals(ClientType.WxMiniApp));
            delContext(clientCtx);
        }
        else
            logger.info("Client has not ChannelHandlerContext channelId:"+ channelId);
    }

    @Override
    protected void addIdentityInfo(ChannelHandlerContext ctx, JSONObject jsonObject)
    {
        jsonObject.put(FieldName.FROM_SERVER, getServerName());
        jsonObject.put(FieldName.CHANNEL_ID, ctx.channel().id().asLongText());
        jsonObject.put(FieldName.CLIENT_IP, ctx.channel().remoteAddress().toString()); //记录客户端登录地址
    }

    @Override
    protected void removeIdentityInfo(JSONObject jsonObject)
    {
        jsonObject.remove(FieldName.FROM_SERVER);
        jsonObject.remove(FieldName.CHANNEL_ID);
        jsonObject.remove(FieldName.CLIENT_IP);
    }
}
