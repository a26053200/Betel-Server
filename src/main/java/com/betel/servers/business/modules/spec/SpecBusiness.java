package com.betel.servers.business.modules.spec;

import com.betel.asd.Business;
import com.betel.consts.FieldName;
import com.betel.servers.business.modules.beans.Spec;
import com.betel.session.Session;
import com.betel.utils.IdGenerator;
import com.betel.utils.TimeUtils;

import java.util.Date;

/**
 * @ClassName: SpecBusiness
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/22 0:46
 */
public class SpecBusiness extends Business<Spec>
{
    @Override
    public Spec newEntry(Session session)
    {
        String nowTime = TimeUtils.date2String(new Date());

        Spec specInfo = new Spec();
        specInfo.setId(Long.toString(IdGenerator.getInstance().nextId()));
        specInfo.setNumber(session.getRecvJson().getString(FieldName.NUMBER));
        specInfo.setName(session.getRecvJson().getString(FieldName.NAME));
        specInfo.setAddTime(nowTime);
        specInfo.setUpdateTime(nowTime);
        return specInfo;
    }

    @Override
    public Spec updateEntry(Session session)
    {
        String nowTime = TimeUtils.date2String(new Date());
        Spec spec = service.getEntryById(session.getRecvJson().getString(FieldName.ID));
        if(spec != null)
        {
            spec.setName(session.getRecvJson().getString(FieldName.NAME));
            spec.setNumber(session.getRecvJson().getString(FieldName.NUMBER));
            spec.setUpdateTime(nowTime);
        }
        return spec;
    }
}
