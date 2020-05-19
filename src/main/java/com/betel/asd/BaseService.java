package com.betel.asd;

import com.betel.spring.IRedisService;

import java.util.List;

/**
 * @ClassName: BaseService
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/18 0:23
 */
public class BaseService<T extends BaseVo> implements IRedisService<T>
{
    private RedisDao<T> dao;

    public RedisDao<T> getDao()
    {
        return dao;
    }
    public void setDao(RedisDao dao)
    {
        this.dao = dao;
    }

    @Override
    public boolean addEntity(T t)
    {
        return dao.addEntity(t);
    }

    @Override
    public boolean batchAddEntity(List<T> datas)
    {
        return dao.batchAddEntity(datas);
    }

    @Override
    public T getEntity(String id)
    {
        return dao.getEntity(id);
    }

    @Override
    public List<T> getViceEntities(String viceId)
    {
        return dao.getViceEntities(viceId);
    }

    @Override
    public boolean updateEntity(T t)
    {
        return dao.updateEntity(t);
    }

    @Override
    public void deleteEntity(List<String> keys)
    {
        dao.deleteEntity(keys);
    }

    @Override
    public void deleteEntity(String id)
    {
        dao.deleteEntity(id);
    }
}
