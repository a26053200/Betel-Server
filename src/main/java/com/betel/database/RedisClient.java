package com.betel.database;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 * @ClassName: RedisClient
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/8 22:40
 */
public class RedisClient
{
    private static final Logger logger = Logger.getLogger(RedisClient.class);

    private static RedisClient s_instance = null;

    public static RedisClient getInstance()
    {
        if (s_instance == null)
            s_instance = new RedisClient();
        return s_instance;
    }

    private Jedis jedis;

    public RedisClient()
    {
    }

    public Jedis connectDB(String host, int port)
    {
        if (jedis == null)
            jedis = new Jedis(host, port);
        logger.info(String.format("Redis Database connection success! connection info:%s:%d", host, port));
        return jedis;
    }

    public Jedis getDB(int index)
    {
        jedis.select(index);
        return jedis;
    }
}
