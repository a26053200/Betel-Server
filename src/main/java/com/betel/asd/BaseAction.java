package com.betel.asd;


import com.alibaba.fastjson.JSONObject;
import com.betel.common.Monitor;
import com.betel.consts.ErrorCode;
import com.betel.consts.FieldName;
import com.betel.session.Session;
import com.betel.utils.BytesUtils;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;

/**
 * @ClassName: BaseAction
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/18 0:23
 */
public abstract class BaseAction<T>
{
    protected Monitor monitor;

    protected HashMap<String, Process> processMap;

    public Monitor getMonitor()
    {
        return monitor;
    }

    public BaseAction()
    {
        processMap = new HashMap<>();
    }

    protected void registerProcess(String operate, String bean, Process process)
    {
        processMap.put(operate + "_" + bean, process);
    }

    public Process getProcess(String method)
    {
        String[] methods = method.split("_");
        String operate = methods[0];
        String bean = methods[1];
        return processMap.get(operate + "_" + bean);
    }

    //常规业务
    public void ActionHandler(ChannelHandlerContext ctx, JSONObject jsonObject, String method)
    {
        Session session = new Session(ctx, jsonObject);
        Process process = getProcess(method);
        if (process == null)
        {
            otherBusiness(session,method);
        }else{
            process.done(session);
        }
    }

    //推送业务
    public void OnPushHandler(ChannelHandlerContext ctx, JSONObject jsonObject, String method)
    {

    }
    //其他非常规业务
    public void otherBusiness(Session session, String method)
    {
        rspdClientError(session, ErrorCode.E0002);
    }

    //返回给客户端错误信息
    public void rspdClientError(Session session, String error)
    {

    }
    //回应客户端请求 带数据体 (到底转发给谁,由具体Monitor决定)
    public void rspdClient(Session session)
    {
        rspdClient(session, null);
    }
    //回应客户端请求 带数据体 (到底转发给谁,由具体Monitor决定)
    public void rspdClient(Session session, JSONObject sendJson)
    {
        String channelId = session.getChannelId();
        JSONObject rspdJson = new JSONObject();
        rspdJson.put(FieldName.SERVER, session.getFromServer());
        rspdJson.put(FieldName.ACTION, session.getRqstAction());
        rspdJson.put(FieldName.CHANNEL_ID, channelId);
        rspdJson.put(FieldName.STATE, session.getState().ordinal());
        if (sendJson != null)
            rspdJson.put(FieldName.DATA, sendJson);

        session.getContext().channel().writeAndFlush(BytesUtils.packBytes(BytesUtils.string2Bytes(rspdJson.toString())));
    }
}
