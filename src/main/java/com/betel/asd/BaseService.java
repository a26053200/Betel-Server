package com.betel.asd;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.interfaces.IVo;
import com.betel.consts.ErrorCode;
import com.betel.consts.FieldName;
import com.betel.event.EventDispatcher;
import com.betel.servers.http.HttpServer;
import com.betel.session.Session;
import com.betel.spring.IRedisService;
import com.betel.utils.BytesUtils;
import com.betel.utils.DBUtils;
import com.betel.utils.TimeUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zhengnan
 * @Date 2020/6/13
 */
public class BaseService<T extends IVo> implements IRedisService<T>
{
    final static Logger logger = LogManager.getLogger(BaseService.class);

    @Autowired
    protected RedisTemplate<String, Serializable> redisTemplate;

    protected EventDispatcher eventDispatcher;

    public EventDispatcher getEventDispatcher()
    {
        return eventDispatcher;
    }

    public void setEventDispatcher(EventDispatcher eventDispatcher)
    {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public RedisDao<T> getDao()
    {
        return null;
    }

    @Override
    public void setTableName(String tableName)
    {
        getDao().setTableName(tableName);
    }


    //加载完成
    public void OnLoaded()
    {

    }

    //推送业务
    public void OnPushHandler(Session session, String method)
    {

    }

    //返回给客户端错误信息
    public void rspdMessage(Session session, String error)
    {
        JSONObject rspdJson = new JSONObject();
        rspdJson.put(FieldName.MSG, error);
        rspdClient(session, rspdJson);
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

    protected <V> List<V> getValueList(String key, int start, int end)
    {
        List<V> resList = new ArrayList<>();
        ListOperations<String, Serializable> listOperations = redisTemplate.opsForList();
        long size = listOperations.size(key);
        List<Serializable> list = listOperations.range(key, start, end < size ? end : size - 1);
        if (list.size() > 0)
        {
            try
            {
                for (int i = 0; i < list.size(); ++i)
                {
                    String json = DBUtils.serializeToString(list.get(i));
                    V t = (V) DBUtils.deserializeToObject(json);
                    resList.add(t);
                }
            } catch (Exception var10)
            {
                var10.printStackTrace();
            }
        } else
        {
            logger.error(String.format("DB has not entities that key == %s", key));
        }
        return resList;
    }

    protected String now()
    {
        return TimeUtils.date2String(new Date());
    }
}
