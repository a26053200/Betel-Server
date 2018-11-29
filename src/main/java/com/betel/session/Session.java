package com.betel.session;

import com.alibaba.fastjson.JSONObject;
import com.betel.consts.Action;
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
    //接收到请参数
    private ParamParser param;
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

        if(recvJson.containsKey("data"))
            this.param = new ParamParser(recvJson);
        if(recvJson.containsKey("client"))
            client = recvJson.getString("client");
        this.channelId = recvJson.getString(FieldName.CHANNEL_ID);
        this.rqstAction = recvJson.getString(Action.NAME);
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

    public ParamParser getParam()
    {
        return param;
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
}
