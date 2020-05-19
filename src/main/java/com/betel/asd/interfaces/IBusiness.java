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
    T newEntity(Session session);

    /**
     * 更新一个实体
     * @param session
     * @return
     */
    T updateEntity(Session session);

    /**
     * 处理业务
     * @param session
     * @param method
     */
    void Handle(Session session, String method);

    /**
     * 处理推送业务
     * @param session
     * @param method
     */
    void OnPushHandle(Session session, String method);
}
