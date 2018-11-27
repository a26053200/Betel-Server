package com.betel.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @ClassName: BaseVo
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/12 0:19
 */
public abstract class BaseVo
{
    protected boolean isEmpty = false;

    protected Jedis db;

    protected String primaryKey;

    public BaseVo()
    {

    }
    public BaseVo(String key)
    {
        primaryKey = key;
    }
    public String getPrimaryKey()
    {
        return primaryKey;
    }

    public boolean isEmpty()
    {
        return isEmpty;
    }


    public abstract JSONObject toJson();

    protected <T extends BaseVo> JSONArray list2jsonArray(List<T> list)
    {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++)
            jsonArray.add(list.get(i).toJson());
        return jsonArray;
    }
}
