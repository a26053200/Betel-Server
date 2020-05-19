package com.betel.spring;

import com.betel.asd.BaseVo;
import com.betel.asd.RedisDao;

import java.util.List;

/**
 * @Description
 * @Author zhengnan
 * @Date 2020/5/19
 */
public interface IRedisService<T extends BaseVo>
{
    /**
     * 获取dao
     * @return
     */
    RedisDao<T> getDao();

    /**
     * 设置dao
     * @param dao
     */
    void setDao(RedisDao<T> dao);
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
     * 不分页的查询副键列表
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
