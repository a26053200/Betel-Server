package com.betel.asd;

import com.betel.asd.interfaces.IBusiness;
import com.betel.common.Monitor;
import com.betel.consts.Bean;
import com.betel.database.RedisKeys;
import com.betel.servers.business.action.ImplAction;
import com.betel.servers.business.modules.beans.Spec;
import com.betel.session.Session;
import com.betel.utils.TimeUtils;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * @ClassName: Business
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/22 0:30
 */
public abstract class Business<T> implements IBusiness<T>
{
    final static Logger logger = Logger.getLogger(Business.class);
    protected ImplAction action;
    protected Monitor monitor;
    protected BaseService<T> service;

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

    protected String now()
    {
        return TimeUtils.date2String(new Date());
    }
}
