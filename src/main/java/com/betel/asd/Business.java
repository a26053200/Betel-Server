package com.betel.asd;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.interfaces.IBusiness;
import com.betel.asd.interfaces.IVo;
import com.betel.common.Monitor;
import com.betel.consts.FieldName;
import com.betel.servers.action.ImplAction;
import com.betel.session.Session;
import com.betel.spring.AbstractBaseRedisDao;
import com.betel.spring.IRedisService;
import com.betel.utils.DBUtils;
import com.betel.utils.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: Business
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/22 0:30
 */
public abstract class Business<T extends IVo> extends AbstractBaseRedisDao<String, Serializable> implements IBusiness
{
    final static Logger logger = LogManager.getLogger(Business.class);
    protected ImplAction action;
    protected Monitor monitor;
    protected IRedisService<T> service;
    protected RedisTemplate<String, Serializable> redisTemplate;

    public void setAction(ImplAction action)
    {
        this.action = action;
        monitor = action.getMonitor();
        service = action.getService();
        //redisTemplate = service.getDao().getRedisTemplate();
    }
    public void OnBusinessReady()
    {

    }

    public ImplAction getAction()
    {
        return action;
    }

    @Override
    public void Handle(Session session, String method)
    {
        logger.error(Business.class.getSimpleName() + " is no Handle service for method:" + method);
    }

    @Override
    public void OnPushHandle(Session session, String method)
    {
        
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
