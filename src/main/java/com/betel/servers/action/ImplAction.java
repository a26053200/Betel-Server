package com.betel.servers.action;

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
public class ImplAction<T extends BaseVo> extends BaseAction<T>
{
    private String bean;

    private IRedisService<T> service;

    private Business<T> business;

    public Business<T> getBusiness()
    {
        return business;
    }

    public IRedisService<T> getService()
    {
        return service;
    }

    public ImplAction(Monitor monitor, String bean, Business<T> business, IRedisService<T> service)
    {
        super();
        this.monitor = monitor;
        this.bean = bean;
        this.business = business;
        this.service = service;
        this.service.setDao(new RedisDao<>());
        this.business.setAction(this);
        //增删改查
//        registerProcess(OperateName.ADD,        bean, new AddEntity());
//        registerProcess(OperateName.QUERY,      bean, new QueryEntity());
//        registerProcess(OperateName.LIST,       bean, new GetEntityList());
//        registerProcess(OperateName.VICE_LIST,  bean, new GetViceEntityList());
//        registerProcess(OperateName.MOD,        bean, new ModEntity());
//        registerProcess(OperateName.DEL,        bean, new DelEntity());
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
    //推送业务
    @Override
    public void OnPushHandler(ChannelHandlerContext ctx, JSONObject jsonObject, String method)
    {
        Session session = new Session(ctx, jsonObject);
        this.business.OnPushHandle(session,method);
    }
    class QueryEntity extends Process<T>
    {
        @Override
        public void done(Session session)
        {
//            String id = session.getRecvJson().getString(FieldName.ID);
//            T bean = service.getEntity(id);
//            JSONObject sendJson = new JSONObject();
//            sendJson.put(FieldName.BEAN_INFO, JsonUtils.object2Json(bean));
//            rspdClient(session, sendJson);
        }
    }

    class AddEntity extends Process<T>
    {
        @Override
        public void done(Session session)
        {
//            T bean = business.newEntity(session);
//            service.addEntity(bean);
//            JSONObject sendJson = new JSONObject();
//            sendJson.put(FieldName.BEAN_INFO, JsonUtils.object2Json(bean));
//            rspdClient(session, sendJson);
        }
    }

    class GetEntityList extends Process
    {
        @Override
        public void done(Session session)
        {
//            JSONObject sendJson = new JSONObject();
//            Iterator<T> it = service.getEntities().iterator();
//            JSONArray array = new JSONArray();
//            int count = 0;
//            while (it.hasNext())
//            {
//                JSONObject item = JsonUtils.object2Json(it.next());
//                item.put(FieldName.KEY, Integer.toString(count));
//                array.add(count++, item);
//            }
//            sendJson.put(FieldName.BEAN_LIST, array);
//            rspdClient(session, sendJson);
        }
    }

    class GetViceEntityList extends Process
    {
        @Override
        public void done(Session session)
        {
//            String viceId = session.getRecvJson().getString(business.getViceKey());
//            JSONObject sendJson = new JSONObject();
//            Iterator<T> it = service.getViceEntities(viceId).iterator();
//            JSONArray array = new JSONArray();
//            int count = 0;
//            while (it.hasNext())
//            {
//                JSONObject item = JsonUtils.object2Json(it.next());
//                item.put(FieldName.KEY, Integer.toString(count));
//                array.add(count++, item);
//            }
//            sendJson.put(FieldName.BEAN_LIST, array);
//            rspdClient(session, sendJson);
        }
    }

    class DelEntity extends Process
    {
        @Override
        public void done(Session session)
        {
//            JSONObject sendJson = new JSONObject();
//            String viceKey = business.getViceKey();
//            String id = session.getRecvJson().getString(FieldName.ID);
//            String key = id;
//            if(!"".equals(viceKey))
//            {
//                String vid = session.getRecvJson().getString(viceKey);
//                key = id + RedisKeys.SPLIT + vid;
//            }
//            boolean success = service.deleteEntity(key);
//            if(success)
//                session.setState(SessionState.Success);
//            else
//                session.setState(SessionState.Fail);
//            rspdClient(session, sendJson);
        }
    }

    class ModEntity extends Process
    {
        @Override
        public void done(Session session)
        {
//            JSONObject sendJson = new JSONObject();
//            T t = business.updateEntity(session);
//            if(t != null)
//            {
//                service.updateEntity(t);
//                session.setState(SessionState.Success);
//            }else{
//                session.setState(SessionState.Fail);
//            }
//            rspdClient(session, sendJson);
        }
    }
}
