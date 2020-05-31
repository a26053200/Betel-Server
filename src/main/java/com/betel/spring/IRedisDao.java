package com.betel.spring;


import com.betel.asd.interfaces.IVo;

import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Author zhengnan
 * @Date 2020/5/19
 */
public interface IRedisDao<T extends IVo>
{
    /**
     * 分页的查询
     * @param baseQuery
     * @return
     */
    //PageResult<T> getPageResult(final BaseQuery baseQuery);

    /**
     * 查询某一张表的总的记录数
     */
    //int getCount(final BaseQuery baseQuery);

    void setTableName(String tableName);
    /**
     * 添加
     * @param t
     */
    boolean addEntity(T t);

    /**
     * 批量添加
     * @param datas
     */
    boolean batchAddEntity(final List<T> datas);

    /**
     * 查询一个
     * @return
     */
    T getEntity(String id);

    /**
     * 查询列表
     * @param key 集合key
     * @return
     */
    List<T> getEntities(String key);

    /**
     * 查询副键列表
     * @return
     */
    List<T> getViceEntities(String viceId);

    /**
     * 修改
     * @param t
     */
    boolean updateEntity(T t);

    /**
     * 根据ids删除一些数据
     * @param keys
     */
    void deleteEntity(List<String> keys);

    /**
     * 根据id删除一条数据
     * @param id
     */
    void deleteEntity(String id);
}
