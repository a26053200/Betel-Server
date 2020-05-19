package com.betel.asd;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.interfaces.IDao;
import com.betel.database.RedisKeys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    final static Logger logger = LogManager.getLogger(BaseDao.class);
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

    private String getFullKey(JSONObject json)
    {
        //获取副键,如果有
        String viceKey = RedisKeys.NONE.equals(viceKeyField) ? RedisKeys.NONE : RedisKeys.SPLIT + json.getString(viceKeyField);
        String key = tableName + RedisKeys.SPLIT + json.getString(RedisKeys.ID) + viceKey;
        return key;
    }
    @Override
    public void addEntity(T t)
    {
        JSONObject json = (JSONObject)JSONObject.toJSON(t);
        String key = getFullKey(json);
        db.set(key,json.toJSONString());
        logger.info(String.format("Table '%s' has added a entry. id == %s",tableName ,json.get(RedisKeys.ID)));
    }

    @Override
    public T getEntityById(String id)
    {
        if(RedisKeys.NONE.equals(viceKeyField))
        {//没有副键
            String key = tableName + RedisKeys.SPLIT + id;
            if (db.exists(key))
            {
                T t = JSONObject.toJavaObject(JSONObject.parseObject(db.get(key)),clazz);
                return t;
            }
            else//查不到该记录
            {
                logger.error(String.format("Table '%s' has not entry that id == %s",tableName,id));
                return null;
            }
        }else{
            String key = tableName + RedisKeys.SPLIT + id + RedisKeys.SPLIT + RedisKeys.WILDCARD;
            List<T> list = getEntityList(db.keys(key));
            if (list.size() > 0)
            {
                return list.get(0);
            }else{//查不到该记录
                logger.error(String.format("Table '%s' has not entry that id == %s, vid == ???",tableName,id));
                return null;
            }
        }
    }

    @Override
    public Set<T> getEntitiesByIds(String[] ids)
    {
        Set<T> set = new HashSet<>();
        for(String id : ids)
        {
            T t = getEntityById(id);
            set.add(t);
        }
        return set;
    }

    @Override
    public List<T> getEntities()
    {
        String key = tableName + RedisKeys.SPLIT + RedisKeys.WILDCARD;
        return getEntityList(db.keys(key));
    }

    @Override
    public List<T> getViceEntities(String viceId)
    {
        String key = tableName + RedisKeys.SPLIT + RedisKeys.WILDCARD + RedisKeys.SPLIT + viceId;
        return getEntityList(db.keys(key));
    }

    private List<T> getEntityList(Set<String> keySet)
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
    public void updateEntity(T t)
    {
        JSONObject json = (JSONObject)JSONObject.toJSON(t);
        String key = getFullKey(json);
        db.set(key,json.toJSONString());
    }

    @Override
    public void deleteEntriesByIDS(String[] ids)
    {
        for(String id : ids)
            deleteEntity(id);
    }

    @Override
    public boolean deleteEntity(String id)
    {
        if(RedisKeys.NONE.equals(viceKeyField))
        {
            String key = tableName + RedisKeys.SPLIT + id;
            if (db.exists(key))
            {
                db.del(key);//移除记录
                logger.info(String.format("Table '%s' has deleted a entry. id == %s",tableName,id));
                return true;
            }
            else//查不到该记录
            {
                logger.error(String.format("Table '%s' has not entry that id == %s",tableName,id));
                return false;
            }
        }else{
            String key = tableName + RedisKeys.SPLIT + id + RedisKeys.SPLIT + RedisKeys.WILDCARD;
            Set<String> keySet = db.keys(key);
            Iterator<String> it = keySet.iterator();
            if (it.hasNext())
            {
                String primaryKey = it.next();
                db.del(primaryKey);//移除记录
                logger.info(String.format("Table '%s' A entry has deleted. id == %s",tableName,id));
                return true;
            }else{//查不到该记录
                logger.error(String.format("Table '%s' has not entry that id == %s, vid == ???",tableName,id));
                return false;
            }
        }
    }
}
