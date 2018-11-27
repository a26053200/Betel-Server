package com.betel.common;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.BaseAction;
import com.betel.servers.business.action.ImplAction;
import com.betel.utils.BytesUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @ClassName: Monitor
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/8 22:48
 */
public abstract class Monitor
{
    private final static Logger logger = Logger.getLogger(Monitor.class);

    /**
     * 多个客户端链接通道
     */
    protected ChannelGroup channelGroup;
    /**
     * 子Monitor
     */
    protected HashMap<String, ChannelHandlerContext> contextMap;
    /**
     * 数据库
     */
    protected Jedis db;

    /**
     * Actions
     */
    protected HashMap<String, ImplAction<?>> actionMap;

    public ChannelGroup getChannelGroup()
    {
        return channelGroup;
    }

    public Channel getChannel(ChannelId channelId)
    {
        return channelGroup.find(channelId);
    }

    public ChannelHandlerContext getContext(String channelId)
    {
        return contextMap.get(channelId);
    }

    public ChannelHandlerContext regContext(ChannelHandlerContext ctx)
    {
        String chId = ctx.channel().id().asLongText();
        if(!contextMap.containsKey(chId))
            contextMap.put(chId,ctx);
        return ctx;
    }

    public void delContext(ChannelHandlerContext ctx)
    {
        String chId = ctx.channel().id().asLongText();
        if(contextMap.containsKey(chId))
            contextMap.remove(chId,ctx);
    }

    public Monitor()
    {
        //所有已经链接的通道,用于广播
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        contextMap = new HashMap<>();
        actionMap = new HashMap<>();
        //初始化数据库
        initDB();
    }

    public ImplAction getAction(String name)
    {
        return actionMap.get(name);
    }

    public Jedis getDB()
    {
        return db;
    }

    // 接收客户端发来的字节,然后转换为json
    public void recvByteBuf(ChannelHandlerContext ctx, ByteBuf buf)
    {
        int msgLen = buf.readableBytes();
        long packHead = buf.readUnsignedInt();
        String json = BytesUtils.readString(buf, (int) packHead);
        logger.info(String.format("[recv] msgLen:%d json:%s", msgLen, json));
        //已知JSONObject,目标要转换为json字符串
        try
        {
            JSONObject jsonObject = JSONObject.parseObject(json);
            RespondJson(ctx, jsonObject);
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
    }
    // 接收客户端发来的字节,然后转换为json
    public void recvByteBuf(ChannelHandlerContext ctx, ByteBuf buf, long packLen)
    {
        int msgLen = buf.readableBytes();
        String json = BytesUtils.readString(buf, (int) packLen);
        logger.info(String.format("[recv] msgLen:%d json:%s", msgLen, json));
        //已知JSONObject,目标要转换为json字符串
        try
        {
            JSONObject jsonObject = JSONObject.parseObject(json);
            RespondJson(ctx, jsonObject);
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
    }
    // 接收服务器之间的数据,直接可以转化为json
    public void recvJsonBuff(ChannelHandlerContext ctx, ByteBuf buf)
    {
        String json = BytesUtils.readString(buf);
        logger.info(String.format("[recv] json:%s",  json));
        //不知道为什么 以后查
        while(!json.startsWith("{"))
        {
            logger.info("Receive json buff 首字符异常:" + json);
            json = json.substring(1);//当收到json
            logger.info("Receive json buff 纠正首字母:" + json);
        }
        logger.info(String.format("[recv] json:%s",  json));
        recvJson(ctx,json);
    }

    protected void recvJson(ChannelHandlerContext ctx, String json)
    {
        try
        {
            JSONObject jsonObject = JSONObject.parseObject(json);
            RespondJson(ctx, jsonObject);
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
    }

    public void recvWxAppJson(ChannelHandlerContext ctx, String json)
    {
        try
        {
            if(json.startsWith("?"))
            {
                logger.warn("Receive weixin App json 首字符异常:" + json);
                json = json.substring(1);//去掉问号
                logger.warn("Receive weixin App json 去掉问号:" + json);
            }
            if(!json.startsWith("{") && !json.endsWith("{"))// 加上花括号
            {
                logger.warn("Receive weixin App json 首字符异常:" + json);
                json = "{" + json+ "}";
                logger.warn("Receive weixin App json 加上花括号:" + json);
            }

            JSONObject jsonObject = JSONObject.parseObject(json);
            RespondJson(ctx, jsonObject);
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * @param ctx
     * @param jsonObject
     */
    protected abstract void RespondJson(ChannelHandlerContext ctx, JSONObject jsonObject);

    /**
     * 初始化数据库
     */
    protected abstract void initDB();

    protected void sendBytes(Channel channel, byte[] bytes)
    {
        byte[] lenBytes = BytesUtils.intToByteArray(bytes.length);
        byte[] mergeBytes = new byte[bytes.length + lenBytes.length];
        //合并字节
        System.arraycopy(lenBytes, 0, mergeBytes, 0, lenBytes.length);
        System.arraycopy(bytes, 0, mergeBytes, lenBytes.length, bytes.length);
        channel.writeAndFlush(mergeBytes);
    }
}
