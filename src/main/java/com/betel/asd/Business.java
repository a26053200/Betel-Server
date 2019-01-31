package com.betel.asd;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.interfaces.IBusiness;
import com.betel.common.Monitor;
import com.betel.consts.Action;
import com.betel.consts.FieldName;
import com.betel.servers.action.ImplAction;
import com.betel.session.Session;
import com.betel.utils.BytesUtils;
import com.betel.utils.TimeUtils;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashMap;

/**
 * @ClassName: Business
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/22 0:30
 */
public abstract class Business<T> implements IBusiness<T>
{
    final static Logger logger = LogManager.getLogger(Business.class);
    protected ImplAction action;
    protected Monitor monitor;
    protected BaseService<T> service;
    private HashMap<String, T> beanMap;

    public Business()
    {
        beanMap = new HashMap<>();
    }

    public T putBean(String channelId, T t)
    {
        return beanMap.put(channelId, t);
    }

    public T getBeanByChannelId(String channelId)
    {
        return beanMap.get(channelId);
    }

    public T removeBean(String channelId)
    {
        return beanMap.remove(channelId);
    }
    @Override
    public String getViceKey()
    {
        return "";
    }

    public void setAction(ImplAction action)
    {
        this.action = action;
        monitor = action.getMonitor();
        service = action.getService();
    }

    @Override
    public T newEntry(Session session)
    {
        return null;
    }

    @Override
    public T updateEntry(Session session)
    {
        return null;
    }

    @Override
    public void Handle(Session session, String method)
    {
        logger.error(Business.class.getSimpleName() + " is no Handle service for method:" + method);
    }

    protected void rspdMessage(Session session, String msg)
    {
        JSONObject rspdJson = new JSONObject();
        rspdJson.put(FieldName.MSG, msg);
        action.rspdClient(session, rspdJson);
    }


    protected String now()
    {
        return TimeUtils.date2String(new Date());
    }
}
