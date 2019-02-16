package com.betel.servers.forward;

import com.alibaba.fastjson.JSONObject;
import com.betel.common.Monitor;
import com.betel.config.ServerConfigVo;
import com.betel.consts.Action;
import com.betel.consts.ClientType;
import com.betel.consts.FieldName;
import com.betel.utils.BytesUtils;
import com.betel.utils.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * @ClassName: ForwardMonitor
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/3 22:27
 */
public abstract class ForwardMonitor extends Monitor
{
    final static Logger logger = LogManager.getLogger(ForwardMonitor.class);
    /**
     * 服务器的客户端
     */
    protected ServerClient serverClient;

    /**
     * 转发的上下文map表
     */
    protected HashMap<String, ForwardContext> contextMap;


    public void setServerClient(ServerClient serverClient)
    {
        this.serverClient = serverClient;
    }

    public ForwardMonitor(ServerConfigVo serverCfgInfo)
    {
        super(serverCfgInfo);
        contextMap = new HashMap<>();
    }

    public ForwardContext getContext(String channelId)
    {
        return contextMap.get(channelId);
    }

    public void addContext(ChannelHandlerContext ctx,String clientType)
    {
        String chId = ctx.channel().id().asLongText();
        if(!contextMap.containsKey(chId))
            contextMap.put(chId,new ForwardContext(ctx,clientType));
    }

    public void delContext(ForwardContext ctx)
    {
        String chId = ctx.getChannelHandlerContext().channel().id().asLongText();
        if(contextMap.containsKey(chId))
            contextMap.remove(chId,ctx);
    }

    public void handshake(Channel channel)
    {
        JSONObject sendJson = new JSONObject();
        sendJson.put(FieldName.SERVER, getCerverCfgInfo().getCenterServerName());
        sendJson.put(FieldName.HANDSHAKE_SERVER, getServerName());
        sendJson.put(FieldName.ACTION, Action.HANDSHAKE);
        String jsonString = sendJson.toString();
        channel.writeAndFlush(BytesUtils.packBytes(BytesUtils.string2Bytes(jsonString)));
    }
    @Override
    protected void RespondJson(ChannelHandlerContext ctx, JSONObject jsonObject)
    {
        String server = jsonObject.get(FieldName.SERVER).toString();
        if (server.equals(getServerName()))
        {//当前服务器直接处理,然后返回给客户端
            forward2Client(jsonObject);
        }else{
            String clientType = jsonObject.getString(FieldName.CLIENT);
            if (StringUtils.isNullOrEmpty(clientType))
                clientType = ClientType.Default;
            addIdentityInfo(ctx, jsonObject);
            addContext(ctx,clientType);
            forward2DestServer(jsonObject);
        }
    }

    //给json添加一些身份信息
    protected void addIdentityInfo(ChannelHandlerContext ctx, JSONObject jsonObject)
    {
        jsonObject.put(FieldName.FROM_SERVER, getServerName());
        jsonObject.put(FieldName.CHANNEL_ID, ctx.channel().id().asLongText());
    }

    //移除json身份信息
    protected void removeIdentityInfo(JSONObject jsonObject)
    {
        jsonObject.remove(FieldName.CHANNEL_ID);
        jsonObject.remove(FieldName.SERVER);
    }

    protected abstract void forward2Client(JSONObject jsonObject);

    // 转发给均衡服务器
    protected void forward2DestServer(JSONObject jsonObject)
    {
        byte[] bytes = BytesUtils.string2Bytes(jsonObject.toString());
        sendBytes(serverClient.getChannel(),bytes);
    }

    //只发送给服务器
    @Override
    public void sendToServer(String serverName,String action,JSONObject data)
    {
        data.put(FieldName.SERVER, serverName);
        data.put(FieldName.ACTION, action);
        String jsonString = data.toString();
        logger.info("[Send to Server]" + jsonString);
        serverClient.getChannel().writeAndFlush(BytesUtils.packBytes(BytesUtils.string2Bytes(jsonString)));
    }

    //推送给客户端
    @Override
    public void pushToClient(String channelId, String serverName, String action, JSONObject data)
    {
        JSONObject sendJson = new JSONObject();
        sendJson.put(FieldName.SERVER, serverName);
        sendJson.put(FieldName.CHANNEL_ID, channelId);
        sendJson.put(FieldName.ACTION, action);
        sendJson.put(FieldName.DATA,data);
        String jsonString = sendJson.toString();
        logger.info("[Push to Client]" + jsonString);
        serverClient.getChannel().writeAndFlush(BytesUtils.packBytes(BytesUtils.string2Bytes(jsonString)));
    }
}
