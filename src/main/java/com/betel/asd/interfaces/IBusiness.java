package com.betel.asd.interfaces;

import com.betel.session.Session;

public interface IBusiness
{
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
