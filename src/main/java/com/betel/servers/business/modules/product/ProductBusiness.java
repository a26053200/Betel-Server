package com.betel.servers.business.modules.product;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.consts.FieldName;
import com.betel.servers.business.modules.beans.Product;
import com.betel.servers.business.modules.beans.Record;
import com.betel.session.Session;
import com.betel.utils.IdGenerator;
import com.betel.utils.TimeUtils;

import java.util.Date;

/**
 * @ClassName: ProductBusiness
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/26 0:53
 */
public class ProductBusiness extends Business<Product>
{
    @Override
    public Product newEntry(Session session)
    {
        String nowTime = TimeUtils.date2String(new Date());
        JSONObject sendJson = session.getRecvJson();
        Product productInfo = new Product();
        productInfo.setId(Long.toString(IdGenerator.getInstance().nextId()));
        productInfo.setName(sendJson.getString(FieldName.NAME));
        productInfo.setAddTime(nowTime);
        productInfo.setUpdateTime(nowTime);
        return productInfo;
    }

    @Override
    public Product updateEntry(Session session)
    {
        String nowTime = TimeUtils.date2String(new Date());
        JSONObject sendJson = session.getRecvJson();
        Product product = service.getEntryById(sendJson.getString(FieldName.ID));
        if(product != null)
        {
            product.setName(session.getRecvJson().getString(FieldName.NAME));
            product.setUpdateTime(nowTime);
        }
        return product;
    }
}
