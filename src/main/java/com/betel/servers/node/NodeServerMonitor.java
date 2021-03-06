package com.betel.servers.node;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.BaseAction;
import com.betel.asd.BaseService;
import com.betel.asd.Business;
import com.betel.asd.interfaces.IVo;
import com.betel.config.ServerConfigVo;
import com.betel.consts.FieldName;
import com.betel.event.EventDispatcher;
import com.betel.servers.action.ImplAction;
import com.betel.servers.forward.ForwardContext;
import com.betel.servers.forward.ForwardMonitor;
import com.betel.session.Session;
import com.betel.spring.IRedisService;
import com.betel.utils.BytesUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.function.BiConsumer;

/**
 * @ClassName: NodeServerMonitor
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/6 0:27
 */
public class NodeServerMonitor extends ForwardMonitor
{
    final static Logger logger = LogManager.getLogger(NodeServerMonitor.class);

    public NodeServerMonitor(ServerConfigVo serverCfgInfo)
    {
        super(serverCfgInfo);
    }

    protected <T extends IVo> void pushService(Class<T> clazz, BaseService<T> service)
    {
        String beanName = clazz.getSimpleName().toLowerCase();// 统一小写
        actionMap.put(beanName, new ImplAction(this, service));
    }

    public void OnAllServiceLoaded()
    {
        // 迭代值
        for (ImplAction action : actionMap.values()) {
            action.getService().OnLoaded();
        }

    }

    @Override
    protected void RespondJson(ChannelHandlerContext ctx, JSONObject jsonObject)
    {
        if (jsonObject.containsKey(FieldName.ACTION))
        {
            String actionParam = jsonObject.getString(FieldName.ACTION);
            String[] actions = actionParam.split("@");
            logger.info("Recv action: " + actionParam);
            String actionName = actions[0].toLowerCase();// 统一小写
            String actionMethod = actions.length > 1 ? actions[1] : FieldName.ACTION;
            if (FieldName.PUSH.equals(actionName))
            {
                OnPushHandler(ctx, jsonObject, actionParam);
            }else{
                ImplAction action = getAction(actionName);
                if (action != null)
                {
                    //action.ActionHandler(ctx, jsonObject, actionMethod);
                    Method[] methods = action.getService().getClass().getDeclaredMethods();
                    boolean invoked = false;
                    for (Method method : methods)
                    {
                        if(method.getName().equals(actionMethod))
                        {
                            try
                            {
                                Session session = new Session(ctx, jsonObject);
                                method.setAccessible(true);//设置为true可调用类的私有方法
                                method.invoke(action.getService(), session);
                            } catch (IllegalAccessException e)
                            {
                                e.printStackTrace();
                            } catch (InvocationTargetException e)
                            {
                                e.printStackTrace();
                            }
                            invoked = true;
                            break;
                        }
                    }
                    if(!invoked)
                        logger.error("There is no method for business:" + actionParam);
                }
                else
                    logger.error("There is no action service for action:" + actionParam);
            }
        }else{
            logger.error("There is no action service for receive json:" + jsonObject.toString());
        }
    }

    //推送消息
    protected void OnPushHandler(ChannelHandlerContext ctx, JSONObject jsonObject, String method)
    {
        Iterator<String> it = actionMap.keySet().iterator();
        while (it.hasNext())
        {
            BaseAction action = getAction(it.next());
            action.OnPushHandler(ctx, jsonObject, method);
        }
    }

    @Override
    protected void forward2Client(JSONObject jsonObject)
    {
        String channelId = jsonObject.getString(FieldName.CHANNEL_ID);
        removeIdentityInfo(jsonObject);
        ForwardContext clientCtx = getContext(channelId);
        if (clientCtx != null)
        {
            byte[] bytes = BytesUtils.packBytes(BytesUtils.string2Bytes(jsonObject.toString()));
            sendBytes(clientCtx.getChannelHandlerContext().channel(),bytes);
            delContext(clientCtx);
        }
        else
            logger.info("Client has not ChannelHandlerContext");
    }
}
