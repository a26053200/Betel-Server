package com.betel.servers.business.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.*;
import com.betel.asd.Process;
import com.betel.common.Monitor;
import com.betel.consts.FieldName;
import com.betel.consts.OperateName;
import com.betel.database.RedisKeys;
import com.betel.session.Session;
import com.betel.session.SessionState;
import com.betel.utils.JsonUtils;

import java.util.Iterator;

/**
 * @ClassName: ImplAction
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/22 0:00
 */
public class ImplAction<T> extends BaseAction<T>
{
    private String bean;

    private BaseService<T> service;

    private Business<T> business;

    public Business<T> getBusiness()
    {
        return business;
    }

    public BaseService<T> getService()
    {
        return service;
    }

    public ImplAction(Monitor monitor, String bean, Class<T> clazz, Business<T> business)
    {
        super();
        this.monitor = monitor;
        this.bean = bean;
        this.business = business;

        this.service = new BaseService<T>();
        this.service.setBaseDao(new BaseDao<T>(monitor.getDB(),clazz,business.getViceKey()));
        this.business.setAction(this);
        //增删改查
        registerProcess(OperateName.ADD,        bean, new AddEntry());
        registerProcess(OperateName.QUERY,      bean, new QueryEntry());
        registerProcess(OperateName.LIST,       bean, new GetEntryList());
        registerProcess(OperateName.VICE_LIST,  bean, new GetViceEntryList());
        registerProcess(OperateName.MOD,        bean, new ModEntry());
        registerProcess(OperateName.DEL,        bean, new DelEntry());
    }

    @Override
    public void otherBusiness(Session session, String method)
    {
        business.Handle(session,method);
    }

    //返回给客户端错误信息
    public void rspdClientError(Session session, String error)
    {
        JSONObject sendJson = new JSONObject();
        sendJson.put(FieldName.ERROR, error);
        rspdClient(session, sendJson);
    }

    class QueryEntry extends Process<T>
    {
        @Override
        public void done(Session session)
        {
            String id = session.getRecvJson().getString(FieldName.ID);
            T bean = service.getEntryById(id);
            JSONObject sendJson = new JSONObject();
            sendJson.put(FieldName.BEAN_INFO, JsonUtils.object2Json(bean));
            rspdClient(session, sendJson);
        }
    }

    class AddEntry extends Process<T>
    {
        @Override
        public void done(Session session)
        {
            T bean = business.newEntry(session);
            service.addEntry(bean);
            JSONObject sendJson = new JSONObject();
            sendJson.put(FieldName.BEAN_INFO, JsonUtils.object2Json(bean));
            rspdClient(session, sendJson);
        }
    }

    class GetEntryList extends Process
    {
        @Override
        public void done(Session session)
        {
            JSONObject sendJson = new JSONObject();
            Iterator<T> it = service.getEntrys().iterator();
            JSONArray array = new JSONArray();
            int count = 0;
            while (it.hasNext())
            {
                JSONObject item = JsonUtils.object2Json(it.next());
                item.put(FieldName.KEY, Integer.toString(count));
                array.add(count++, item);
            }
            sendJson.put(FieldName.BEAN_LIST, array);
            rspdClient(session, sendJson);
        }
    }

    class GetViceEntryList extends Process
    {
        @Override
        public void done(Session session)
        {
            String viceId = session.getRecvJson().getString(business.getViceKey());
            JSONObject sendJson = new JSONObject();
            Iterator<T> it = service.getViceEntrys(viceId).iterator();
            JSONArray array = new JSONArray();
            int count = 0;
            while (it.hasNext())
            {
                JSONObject item = JsonUtils.object2Json(it.next());
                item.put(FieldName.KEY, Integer.toString(count));
                array.add(count++, item);
            }
            sendJson.put(FieldName.BEAN_LIST, array);
            rspdClient(session, sendJson);
        }
    }

    class DelEntry extends Process
    {
        @Override
        public void done(Session session)
        {
            JSONObject sendJson = new JSONObject();
            String viceKey = business.getViceKey();
            String id = session.getRecvJson().getString(FieldName.ID);
            String key = id;
            if(!"".equals(viceKey))
            {
                String vid = session.getRecvJson().getString(viceKey);
                key = id + RedisKeys.SPLIT + vid;
            }
            boolean success = service.deleteEntry(key);
            if(success)
                session.setState(SessionState.Success);
            else
                session.setState(SessionState.Fail);
            rspdClient(session, sendJson);
        }
    }

    class ModEntry extends Process
    {
        @Override
        public void done(Session session)
        {
            JSONObject sendJson = new JSONObject();
            T t = business.updateEntry(session);
            if(t != null)
            {
                service.updateEntry(t);
                session.setState(SessionState.Success);
            }else{
                session.setState(SessionState.Fail);
            }
            rspdClient(session, sendJson);
        }
    }
}
