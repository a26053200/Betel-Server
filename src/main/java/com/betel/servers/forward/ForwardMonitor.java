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

import java.util.HashMap;

/**
 * @ClassName: ForwardMonitor
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/3 22:27
 */
public abstract class ForwardMonitor extends Monitor
{
    /**
     * 转发的目标服务器
     */
    protected ServerConfigVo destServerCfg;

    /**
     * 服务器的客户端
     */
    protected ServerClient serverClient;

    /**
     * 转发的上下文map表
     */
    protected HashMap<String, ForwardContext> contextMap;

    public void setDestServerConfig(ServerConfigVo destServerCfg)
    {
        this.destServerCfg = destServerCfg;
    }

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
        sendJson.put(FieldName.SERVER, destServerCfg.getName());
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
            jsonObject.put(FieldName.FROM_SERVER, getServerName());
            jsonObject.put(FieldName.CHANNEL_ID, ctx.channel().id().asLongText());
            addContext(ctx,clientType);
            forward2DestServer(jsonObject);
        }
    }

    protected abstract void forward2Client(JSONObject jsonObject);

    // 转发给均衡服务器
    protected void forward2DestServer(JSONObject jsonObject)
    {
        byte[] bytes = BytesUtils.string2Bytes(jsonObject.toString());
        sendBytes(serverClient.getChannel(),bytes);
    }
}
