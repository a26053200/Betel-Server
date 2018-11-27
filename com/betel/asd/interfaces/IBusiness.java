package com.betel.asd.interfaces;

import com.betel.session.Session;

public interface IBusiness<T>
{
    /**
     * 获取实体副键通配键值
     * @return
     */
    String getViceKey();

    /**
     * 新建一个实体
     * @param session
     * @return
     */
    T newEntry(Session session);

    /**
     * 更新一个实体
     * @param session
     * @return
     */
    T updateEntry(Session session);

    /**
     * 处理业务
     * @param session
     * @param method
     */
    void Handle(Session session, String method);
}
