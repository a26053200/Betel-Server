package com.betel.asd.interfaces;

import java.util.List;
import java.util.Set;

public interface IService<T>
{
    /**
     * 分页的查询
     * @param baseQuery
     * @return
     */
    //public PageResult<T> getPageResult(final BaseQuery baseQuery);
    /**
     * 添加
     *
     * @param t
     */
    public void addEntry(T t);

    /**
     * 查询一个
     *
     * @return
     */
    public T getEntryById(String id);
    /**
     * 按ids查询
     */
    public Set<T> getEntrysByIds(String[] ids);

    /**
     * 不分页的查询
     *
     * @return
     */
    public List<T> getEntrys();

    /**
     * 不分页的查询副键列表
     * @return
     */
    public List<T> getViceEntrys(String viceId);

    /**
     * 修改
     *
     * @param t
     */
    public void updateEntry(T t);

    /**
     * 根据ids删除一些数据
     *
     * @param ids
     */
    public void deleteEntriesByIDS(String[] ids);

    /**
     * 根据id删除一条数据
     *
     * @param id
     */
    public boolean deleteEntry(String id);
}
