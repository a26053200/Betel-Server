package com.betel.asd;

import com.betel.asd.interfaces.IService;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: BaseService
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/18 0:23
 */
public class BaseService<T> implements IService<T>
{
    private BaseDao<T> baseDao;

    public BaseDao<T> getBaseDao()
    {
        return baseDao;
    }
    public void setBaseDao(BaseDao baseDao)
    {
        this.baseDao = baseDao;
    }
    @Override
    public void addEntry(T t)
    {
        this.getBaseDao().addEntry(t);
    }

    @Override
    public T getEntryById(String id)
    {
        return this.getBaseDao().getEntryById(id);
    }

    @Override
    public Set<T> getEntrysByIds(String[] ids)
    {
        return this.getBaseDao().getEntrysByIds(ids);
    }

    @Override
    public List<T> getEntrys()
    {
        return this.getBaseDao().getEntrys();
    }

    @Override
    public List<T> getViceEntrys(String viceId)
    {
        return this.getBaseDao().getViceEntrys(viceId);
    }

    @Override
    public void updateEntry(T t)
    {
        this.getBaseDao().updateEntry(t);
    }

    @Override
    public void deleteEntriesByIDS(String[] ids)
    {
        this.getBaseDao().deleteEntriesByIDS(ids);
    }

    @Override
    public boolean deleteEntry(String id)
    {
        return this.getBaseDao().deleteEntry(id);
    }
}
