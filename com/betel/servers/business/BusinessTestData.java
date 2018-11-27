package com.betel.servers.business;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.database.RedisClient;
import redis.clients.jedis.Jedis;

/**
 * @ClassName: BusinessTestData
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/5 23:08
 */
public class BusinessTestData
{
    public static void main(String[] args)
    {
        //Debug.initLog("[OnKeyConfigServer]");
        //连接数据库
        RedisClient.getInstance().connectDB("127.0.0.1",6379);

        Jedis db = RedisClient.getInstance().getDB(2);

        JSONObject gameServerJson = new JSONObject();
        JSONArray ja = new JSONArray();

        //1服
        JSONObject gameServer1 = new JSONObject();
        gameServer1.put("name","zhengnan1");
        gameServer1.put("host","127.0.0.1");
        gameServer1.put("port",8081);
        ja.add(gameServer1);
        //2服
        JSONObject gameServer2 = new JSONObject();
        gameServer2.put("name","zhengnan2");
        gameServer2.put("host","127.0.0.1");
        gameServer2.put("port",8082);
        ja.add(gameServer2);

        gameServerJson.put("list",ja);
        db.set("GameServer",gameServerJson.toJSONString());
    }
}
