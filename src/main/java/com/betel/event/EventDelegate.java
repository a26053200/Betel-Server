package com.betel.event;

/**
 * @Description
 * @Author zhengnan
 * @Date 2020/5/14
 */
public interface EventDelegate<T extends EventObject>
{
    void invoke(T t);
}
