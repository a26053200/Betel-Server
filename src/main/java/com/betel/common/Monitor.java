package com.betel.common;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.betel.config.ServerConfigVo;
import com.betel.database.RedisClient;
import com.betel.servers.action.ImplAction;
import com.betel.utils.BytesUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.HashMap;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @ClassName: Monitor
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/8 22:48
 */
public abstract class Monitor
{
    final static Logger logger = LogManager.getLogger(Monitor.class);

    /**
     * 服务器配置
     */
    private ServerConfigVo serverCfgInfo;
    /**
     * 多个客户端链接通道
     */
    protected ChannelGroup channelGroup;
    /**
     * 数据库
     */
    protected Jedis db;
    /**
     * Actions
     */
    protected HashMap<String, ImplAction<?>> actionMap;

    public Monitor(ServerConfigVo serverCfgInfo)
    {
        this.serverCfgInfo = serverCfgInfo;

        //所有已经链接的通道,用于广播
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        actionMap = new HashMap<>();
        //初始化数据库
        initDB();
    }

    public String getServerName()
    {
        return serverCfgInfo.getName();
    }

    public ChannelGroup getChannelGroup()
    {
        return channelGroup;
    }

    public Channel getChannel(ChannelId channelId)
    {
        return channelGroup.find(channelId);
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
    private void initDB()
    {
        if (this.serverCfgInfo != null && this.serverCfgInfo.getDbIndex() >= 0)
        {//连接数据库
            RedisClient.getInstance().connectDB(this.serverCfgInfo.getDbHost(),this.serverCfgInfo.getDbPort());
            this.db = RedisClient.getInstance().getDB(this.serverCfgInfo.getDbIndex());
        }
    }

    protected void sendBytes(Channel channel, byte[] bytes)
    {
        byte[] lenBytes = BytesUtils.intToByteArray(bytes.length);
        byte[] mergeBytes = new byte[bytes.length + lenBytes.length];
        //合并字节
        System.arraycopy(lenBytes, 0, mergeBytes, 0, lenBytes.length);
        System.arraycopy(bytes, 0, mergeBytes, lenBytes.length, bytes.length);
        channel.writeAndFlush(mergeBytes);
    }

    /**
     * 直接响应客户端
     * @param channel
     * @param msg
     * @param useJson 是否使用json传输，决定了是否在字符后面加 '\0' 结尾符符号
     */
    public void httpRspd(Channel channel, String msg, boolean useJson)
    {
        logger.info(String.format("[Http Rspd]:%s", msg));
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK, Unpooled.wrappedBuffer(BytesUtils.string2Bytes(msg,false)));
        if (useJson)
            response.headers().set(CONTENT_TYPE, "application/json");
        else
            response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        response.headers().set("Access-Control-Allow-Origin", "*");
        channel.writeAndFlush(response);
    }
}
