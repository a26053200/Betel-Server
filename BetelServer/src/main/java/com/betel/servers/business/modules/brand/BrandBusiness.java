package com.betel.servers.business.modules.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.asd.BaseAction;
import com.betel.asd.Business;
import com.betel.common.Monitor;
import com.betel.consts.Action;
import com.betel.consts.FieldName;
import com.betel.consts.ServerName;
import com.betel.servers.business.modules.beans.Brand;
import com.betel.session.Session;
import com.betel.utils.IdGenerator;
import com.betel.utils.JsonUtils;
import com.betel.utils.TimeUtils;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.Iterator;

/**
 * @ClassName: BuyerAction
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/18 23:00
 */
public class BrandBusiness extends Business<Brand>
{

    @Override
    public Brand newEntry(Session session)
    {
        String nowTime = now();

        Brand brandInfo = new Brand();
        brandInfo.setId(Long.toString(IdGenerator.getInstance().nextId()));
        brandInfo.setName(session.getRecvJson().getString(FieldName.NAME));
        brandInfo.setAddTime(nowTime);
        brandInfo.setUpdateTime(nowTime);
        return  brandInfo;
    }

    @Override
    public Brand updateEntry(Session session)
    {
        Brand brandInfo = service.getEntryById(session.getRecvJson().getString(FieldName.ID));
        if(brandInfo != null)
        {
            brandInfo.setName(session.getRecvJson().getString(FieldName.NAME));
            brandInfo.setUpdateTime(now());
        }
        return brandInfo;
    }
}
