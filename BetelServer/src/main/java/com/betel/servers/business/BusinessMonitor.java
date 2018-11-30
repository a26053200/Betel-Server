package com.betel.servers.business;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.BaseAction;
import com.betel.common.Monitor;
import com.betel.consts.*;
import com.betel.database.RedisClient;
import com.betel.servers.business.action.ImplAction;
import com.betel.servers.business.modules.beans.*;
import com.betel.servers.business.modules.brand.BrandBusiness;
import com.betel.servers.business.modules.buyer.BuyerBusiness;
import com.betel.servers.business.modules.category.CategoryBusiness;
import com.betel.servers.business.modules.profile.ProfileBusiness;
import com.betel.servers.business.modules.record.RecordBusiness;
import com.betel.servers.business.modules.seller.SellerBusiness;
import com.betel.servers.business.modules.spec.SpecBusiness;
import com.betel.servers.business.modules.spec.SpecValueBusiness;
import com.betel.utils.BytesUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * @ClassName: GateMonitor
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/6/8 23:01
 */
public class BusinessMonitor extends Monitor
{
    final static Logger logger = Logger.getLogger(BusinessMonitor.class);

    private BusinessClient businessServerClient;

    public BusinessMonitor()
    {
        super();

        actionMap.put(Bean.RECORD,      new ImplAction<>(this, Bean.RECORD,     Record.class,       new RecordBusiness()));
        actionMap.put(Bean.PROFILE,     new ImplAction<>(this, Bean.PROFILE,    Profile.class,      new ProfileBusiness()));
        actionMap.put(Bean.BUYER,       new ImplAction<>(this, Bean.BUYER,      Buyer.class,        new BuyerBusiness()));
        actionMap.put(Bean.SELLER,      new ImplAction<>(this, Bean.SELLER,     Seller.class,       new SellerBusiness()));
        actionMap.put(Bean.BRAND,       new ImplAction<>(this, Bean.BRAND,      Brand.class,        new BrandBusiness()));
        actionMap.put(Bean.CATEGORY,    new ImplAction<>(this, Bean.CATEGORY,   Category.class,     new CategoryBusiness()));
        actionMap.put(Bean.SPEC,        new ImplAction<>(this, Bean.SPEC,       Spec.class,         new SpecBusiness()));
        actionMap.put(Bean.SPEC_VALUE,  new ImplAction<>(this, Bean.SPEC_VALUE, SpecValue.class,    new SpecValueBusiness()));
    }
    public void SetGameServerClient(BusinessClient businessServerClient)
    {
        this.businessServerClient = businessServerClient;
    }

    public void handshake(Channel channel)
    {
        JSONObject sendJson = new JSONObject();
        sendJson.put(FieldName.SERVER, ServerName.BALANCE_SERVER);
        sendJson.put(Action.NAME, Action.HANDSHAKE_BUSINESS2BALANCE);
        channel.writeAndFlush(BytesUtils.packBytes(BytesUtils.string2Bytes(sendJson.toString())));
    }

    @Override
    protected void initDB()
    {
        db = RedisClient.getInstance().getDB(2);//初始化redis数据库
    }

    @Override
    protected void RespondJson(ChannelHandlerContext ctx, JSONObject jsonObject)
    {
        if (jsonObject.containsKey(Action.NAME))
        {
            String actionParam = jsonObject.getString(Action.NAME);
            String[] actions = actionParam.split("@");
            logger.info("Recv action: " + actionParam);
            String actionName = actions[0];
            String actionMethod = actions.length > 1 ? actions[1] : Action.NONE;
            BaseAction action = actionMap.get(actionName);
            if (action != null)
                action.ActionHandler(ctx, jsonObject, actionMethod);
            else
                logger.error("There is no action service for action:" + actionParam);
        }else{
            logger.error("There is no action service for receive json:" + jsonObject.toString());
        }


    }
}
