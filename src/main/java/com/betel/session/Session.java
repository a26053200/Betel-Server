package com.betel.session;

import com.alibaba.fastjson.JSONObject;
import com.betel.consts.Action;
import com.betel.consts.ClientType;
import com.betel.consts.FieldName;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: Session
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/8/9 23:21
 */
public class Session
{
    //通信信道
    private ChannelHandlerContext context;
    //接收到的json
    private JSONObject recvJson;
    //会话状态
    private SessionStatus status;
    //会话状态
    private SessionState state;
    //信道id
    private String fromServer;
    //信道id
    private String channelId;
    //请求Action
    private String rqstAction;
    //请求Action
    private String client;

    public Session(ChannelHandlerContext ctx, JSONObject recvJson)
    {
        this.context = ctx;
        this.recvJson = recvJson;
        this.state = SessionState.Success;
        this.status = SessionStatus.Free;

        if(recvJson.containsKey(FieldName.CLIENT))
            client = recvJson.getString(FieldName.CLIENT);
        else
            client = ClientType.Default;
        this.fromServer = recvJson.getString(FieldName.FROM_SERVER);
        this.channelId = recvJson.getString(FieldName.CHANNEL_ID);
        this.rqstAction = recvJson.getString(FieldName.ACTION);
    }

    public ChannelHandlerContext getContext()
    {
        return context;
    }

    public JSONObject getRecvJson()
    {
        return recvJson;
    }

    public SessionStatus getStatus()
    {
        return status;
    }

    public SessionState getState()
    {
        return state;
    }

    public void setState(SessionState state)
    {
        this.state = state;
    }

    public void setStatus(SessionStatus status)
    {
        this.status = status;
    }

    public String getChannelId()
    {
        return channelId;
    }

    public String getRqstAction()
    {
        return rqstAction;
    }

    public String getClient()
    {
        return client;
    }

    public String getFromServer()
    {
        return fromServer;
    }
}
