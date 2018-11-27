package com.betel.servers.business.modules.spec;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.consts.FieldName;
import com.betel.database.RedisKeys;
import com.betel.servers.business.modules.beans.Spec;
import com.betel.servers.business.modules.beans.SpecValue;
import com.betel.session.Session;
import com.betel.session.SessionState;
import com.betel.utils.IdGenerator;
import com.betel.utils.TimeUtils;

import java.util.Date;

/**
 * @ClassName: SpecBusiness
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/22 0:46
 */
public class SpecValueBusiness extends Business<SpecValue>
{
    private static final String ViceKey = "specId";
    @Override
    public String getViceKey()
    {
        return ViceKey;
    }
    @Override
    public SpecValue newEntry(Session session)
    {
        String nowTime = TimeUtils.date2String(new Date());

        SpecValue specValueInfo = new SpecValue();
        specValueInfo.setId(Long.toString(IdGenerator.getInstance().nextId()));
        specValueInfo.setSpecId(session.getRecvJson().getString(ViceKey));
        specValueInfo.setValue(session.getRecvJson().getString(FieldName.VALUE));
        specValueInfo.setAddTime(nowTime);
        specValueInfo.setUpdateTime(nowTime);
        return specValueInfo;
    }

    @Override
    public SpecValue updateEntry(Session session)
    {
        JSONObject sendJson = session.getRecvJson();
        String id = sendJson.getString(FieldName.ID);
        String vid = sendJson.getString(ViceKey);
        SpecValue specValueInfo = service.getEntryById(id + RedisKeys.SPLIT + vid);
        if(specValueInfo != null)
        {
            String nowTime = TimeUtils.date2String(new Date());
            specValueInfo.setValue(session.getRecvJson().getString(FieldName.VALUE));
            specValueInfo.setUpdateTime(nowTime);
        }
        return specValueInfo;
    }
}
