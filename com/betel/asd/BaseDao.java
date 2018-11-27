package com.betel.asd;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.interfaces.IDao;
import com.betel.database.RedisKeys;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * @ClassName: BaseDao
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/18 0:16
 */
public class BaseDao<T> implements IDao<T>
{
    final static Logger logger = Logger.getLogger(BaseDao.class);
    protected Jedis db;
    protected Class<T> clazz;

    private String tableName;
    private String viceKeyField;

    public BaseDao(Jedis db, Class<T> clazz, String viceKeyField)
    {
        this.db = db;
        this.clazz = clazz;
        this.tableName = clazz.getSimpleName();
        this.viceKeyField = viceKeyField;
    }

    @Override
    public void addEntry(T t)
    {
        JSONObject json = (JSONObject)JSONObject.toJSON(t);
        //获取副键
        String viceKey = RedisKeys.NOME.equals(viceKeyField)? RedisKeys.NOME : RedisKeys.SPLIT + json.getString(viceKeyField);
        String key = tableName + RedisKeys.SPLIT + json.getString(RedisKeys.ID) + viceKey;
        db.set(key,json.toJSONString());
    }

    @Override
    public T getEntryById(String id)
    {
        String key = tableName + RedisKeys.SPLIT + id;
        if (db.exists(key))
        {
            T t = JSONObject.toJavaObject(JSONObject.parseObject(db.get(key)),clazz);
            return t;
        }
        else//查不到该记录
        {
            logger.error(String.format("There is no entry that id == %s",id));
            return null;
        }
    }

    @Override
    public Set<T> getEntrysByIds(String[] ids)
    {
        Set<T> set = new HashSet<>();
        for(String id : ids)
        {
            T t = getEntryById(id);
            set.add(t);
        }
        return set;
    }

    @Override
    public List<T> getEntrys()
    {
        String key = tableName + RedisKeys.SPLIT + RedisKeys.WILDCARD;
        return getEntryList(db.keys(key));
    }

    @Override
    public List<T> getViceEntrys(String viceId)
    {
        String key = tableName + RedisKeys.SPLIT + RedisKeys.WILDCARD + RedisKeys.SPLIT + viceId;
        return getEntryList(db.keys(key));
    }

    private List<T> getEntryList(Set<String> keySet)
    {
        List<T> list = new ArrayList<>();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext())
        {
            String primaryKey = it.next();
            T t = JSONObject.toJavaObject(JSONObject.parseObject(db.get(primaryKey)),clazz);
            list.add(t);
        }
        return list;
    }

    @Override
    public void updateEntry(T t)
    {
        JSONObject json = (JSONObject)JSONObject.toJSON(t);
        String key = tableName + RedisKeys.SPLIT + json.getString(RedisKeys.ID);
        db.set(key,json.toJSONString());
    }

    @Override
    public void deleteEntriesByIDS(String[] ids)
    {
        for(String id : ids)
            deleteEntry(id);
    }

    @Override
    public boolean deleteEntry(String id)
    {
        String key = tableName + RedisKeys.SPLIT + id;
        if (db.exists(key))
        {
            db.del(key);
            return true;
        }

        else//查不到该记录
        {
            logger.error(String.format("There is no entry that id == %s",id));
            return false;
        }
    }
}
