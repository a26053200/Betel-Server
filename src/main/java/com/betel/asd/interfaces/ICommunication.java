package com.betel.asd.interfaces;

import com.alibaba.fastjson.JSONObject;

/**
 * 通信对象接口
 */
public interface ICommunication<T>
{
    String getChannelId();

    void push(String action, JSONObject json);

    void pushAll(String action, JSONObject json, ICommunicationFilter<T> filter);

    void pushAll(String action, JSONObject json);
}
