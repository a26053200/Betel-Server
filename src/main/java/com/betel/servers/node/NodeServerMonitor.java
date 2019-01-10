package com.betel.servers.node;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.BaseAction;
import com.betel.config.ServerConfigVo;
import com.betel.consts.FieldName;
import com.betel.servers.forward.ForwardContext;
import com.betel.servers.forward.ForwardMonitor;
import com.betel.utils.BytesUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    @Override
    protected void RespondJson(ChannelHandlerContext ctx, JSONObject jsonObject)
    {
        if (jsonObject.containsKey(FieldName.ACTION))
        {
            String actionParam = jsonObject.getString(FieldName.ACTION);
            String[] actions = actionParam.split("@");
            logger.info("Recv action: " + actionParam);
            String actionName = actions[0];
            String actionMethod = actions.length > 1 ? actions[1] : FieldName.ACTION;
            BaseAction action = getAction(actionName);
            if (action != null)
                action.ActionHandler(ctx, jsonObject, actionMethod);
            else
                logger.error("There is no action service for action:" + actionParam);
        }else{
            logger.error("There is no action service for receive json:" + jsonObject.toString());
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
