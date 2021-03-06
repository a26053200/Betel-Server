package com.betel.servers.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.*;
import com.betel.asd.Process;
import com.betel.asd.interfaces.IVo;
import com.betel.common.Monitor;
import com.betel.consts.FieldName;
import com.betel.consts.OperateName;
import com.betel.database.RedisKeys;
import com.betel.servers.node.NodeServerMonitor;
import com.betel.session.Session;
import com.betel.session.SessionState;
import com.betel.spring.IRedisService;
import com.betel.utils.JsonUtils;
import io.netty.channel.ChannelHandlerContext;

import java.util.Iterator;

/**
 * @ClassName: ImplAction
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/22 0:00
 */
public class ImplAction<T extends IVo> extends BaseAction<T>
{
    private BaseService service;

    public BaseService getService()
    {
        return service;
    }

    //public ImplAction(Monitor monitor, String bean, RedisDao<T> redisDao, Business<T> business, IRedisService<T> service)
    public ImplAction(Monitor monitor, BaseService service)
    {
        super();
        this.monitor = monitor;
        this.service = service;
    }

    //返回给客户端错误信息
    public void rspdClientError(Session session, String error)
    {
        JSONObject sendJson = new JSONObject();
        sendJson.put(FieldName.ERROR, error);
        rspdClient(session, sendJson);
    }
    //推送业务
    @Override
    public void OnPushHandler(ChannelHandlerContext ctx, JSONObject jsonObject, String method)
    {
        Session session = new Session(ctx, jsonObject);
        this.service.OnPushHandler(session, method);
    }
}
