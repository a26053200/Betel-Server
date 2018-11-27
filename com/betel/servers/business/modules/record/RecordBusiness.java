package com.betel.servers.business.modules.record;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.BaseAction;
import com.betel.asd.Business;
import com.betel.common.Monitor;
import com.betel.consts.ServerName;
import com.betel.servers.business.modules.beans.Buyer;
import com.betel.servers.business.modules.beans.Record;
import com.betel.utils.IdGenerator;
import com.betel.utils.TimeUtils;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * @ClassName: BuyerAction
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/18 23:00
 */
public class RecordBusiness extends Business<Record>
{
    /**
     * 记录买家登录
     * @param buyer 买家
     * @return
     */
    public Record addBuyerLoginRecord(Buyer buyer, String content)
    {
        // 添加一次登陆记录
        Record record = addRecord(buyer.getId(),"Login",content);
        return record;
    }

    /**
     * 添加一次记录
     * @param recorderId
     * @param type
     * @param content
     * @return
     */
    public Record addRecord(String recorderId,String type,String content)
    {
        long recordId = IdGenerator.getInstance().nextId();
        Record record = new Record();
        record.setId(Long.toString(recordId));
        record.setRecorderId(recorderId);
        record.setType(type);
        record.setContent(content);
        record.setAddTime(TimeUtils.date2String(new Date()));
        service.addEntry(record);
        return record;
    }
}
