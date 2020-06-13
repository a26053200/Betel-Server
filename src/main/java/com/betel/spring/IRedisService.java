package com.betel.spring;

import com.betel.asd.RedisDao;
import com.betel.asd.interfaces.IVo;

import java.util.List;

/**
 * @Description
 * @Author zhengnan
 * @Date 2020/5/19
 */
public interface IRedisService<T extends IVo>
{
    RedisDao<T> getDao();

    void setTableName(String tableName);
}
