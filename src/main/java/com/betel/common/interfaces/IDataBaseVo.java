package com.betel.common.interfaces;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * @ClassName: IDataBaseVo
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/12 0:20
 */
public interface IDataBaseVo
{
    void fromDB(Jedis db);
    void writeDB(Jedis db);
}
