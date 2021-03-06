package com.betel.asd;


import com.betel.asd.interfaces.IVo;
import com.betel.database.RedisKeys;
import com.betel.spring.AbstractBaseRedisDao;
import com.betel.spring.IRedisDao;
import com.betel.utils.DBUtils;
import com.betel.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Author zhengnan
 * @Date 2020/5/19
 */

@Repository
public class RedisDao<T extends IVo> extends AbstractBaseRedisDao<String, Serializable> implements IRedisDao<T>
{
    final static Logger logger = LogManager.getLogger(RedisDao.class);

    private String tableName;

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getFullKey(T t)
    {
        //获取副键,如果有
        String viceKey = StringUtils.isNullOrEmpty(t.getVid()) ? RedisKeys.NONE : RedisKeys.SPLIT + t.getVid();
        return tableName + RedisKeys.SPLIT + t.getId() + viceKey;
    }

    @Override
    public boolean addEntity(T t)
    {
        boolean result = redisTemplate.execute(connection ->
        {
            ValueOperations<String, Serializable> valueOper = redisTemplate.opsForValue();
            valueOper.set(getFullKey(t), t);
            return true;
        }, false, true);
        return result;
    }

    @Override
    public boolean batchAddEntity(final List<T> datas)
    {
        boolean result = redisTemplate.execute(connection ->
        {
            ValueOperations<String, Serializable> valueOper = redisTemplate.opsForValue();
            for (T data : datas)
            {
                valueOper.set(getFullKey(data), data);
            }
            return true;
        }, false, true);
        return result;
    }

    @Override
    public T getEntity(String id)
    {
        String finalKey = tableName + RedisKeys.SPLIT + id + RedisKeys.SPLIT + RedisKeys.WILDCARD;
        List<T> list = getEntities(finalKey);
        if (list.size() > 0)
        {
            return list.get(0);
        } else
        {
            logger.error(String.format("Table '%s' has not entry that id == %s, vid == ???", tableName, id));
            return null;
        }
    }

    @Override
    public List<T> getEntities(final String key)
    {
        Set<String> keys = redisTemplate.keys(key);
        List<T> result = redisTemplate.execute((RedisCallback<List<T>>) connection ->
        {
            List<T> resList = new ArrayList<>();
            ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
            List<Serializable> list = operations.multiGet(keys);
            if (list.size() > 0)
            {
                try
                {
                    for (int i = 0; i < list.size(); i++)
                    {
                        Serializable serializable = list.get(i);
                        T t = (T)serializable;
                        resList.add(t);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else
                logger.error(String.format("Table '%s' has not entities that key == %s", tableName, key));
            return resList;
        });
        return result;
    }

    @Override
    public List<T> getViceEntities(String viceId)
    {
        String key = tableName + RedisKeys.SPLIT + RedisKeys.WILDCARD + RedisKeys.SPLIT + viceId;
        return getEntities(key);
    }

    @Override
    public List<T> getAllEntities()
    {
        String key = tableName + RedisKeys.SPLIT + RedisKeys.WILDCARD + RedisKeys.SPLIT + RedisKeys.WILDCARD;
        return getEntities(key);
    }

    @Override
    public boolean updateEntity(T t)
    {
        String id = t.getId();
        if (getEntity(id) == null)
        {
            throw new NullPointerException("Data has not exist! key = " + id);
        }
        boolean result = redisTemplate.execute((RedisCallback<Boolean>) connection ->
        {
            ValueOperations<String, Serializable> valueOper = redisTemplate.opsForValue();
            valueOper.set(getFullKey(t), t);
            return true;
        });
        return result;
    }

    @Override
    public void deleteEntity(List<String> keys)
    {
        redisTemplate.delete(keys);
    }

    @Override
    public void deleteEntity(String key)
    {
        List<String> list = new ArrayList<>();
        list.add(key);
        deleteEntity(list);
    }

    @Override
    public void deleteEntity(T t)
    {
        List<String> list = new ArrayList<>();
        list.add(getFullKey(t));
        deleteEntity(list);
    }
}
