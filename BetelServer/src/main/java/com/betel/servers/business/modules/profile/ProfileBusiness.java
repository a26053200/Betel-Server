package com.betel.servers.business.modules.profile;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.consts.FieldName;
import com.betel.consts.ServerName;
import com.betel.servers.business.modules.beans.Profile;
import com.betel.session.Session;
import com.betel.utils.StringUtils;
import com.betel.utils.TimeUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;

/**
 * @ClassName: BuyerAction
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/18 23:00
 */
public class ProfileBusiness extends Business<Profile>
{
    final static Logger logger = Logger.getLogger(ProfileBusiness.class);

    private HashMap<String, Profile> profileMap = new HashMap<>();

    // 微信用户登录
    public Profile profileLogin(Session session, JSONObject wxLoginInfoJson)
    {
        String nowTime = TimeUtils.date2String(new Date());
        String channelId = session.getChannelId();
        String openid = wxLoginInfoJson.getString(FieldName.OPEN_ID);// 微信OpenID
        Profile profile = getProfileInfo(channelId);// 用户信息
        if (profile == null)
        {
            profile = service.getEntryById(openid);
            if (profile == null)
            {//买家第一次登陆
                //生成用户Id
                //profile.setId(Long.toString(IdGenerator.getInstance().nextId()));
                profile = new Profile();
                profile.setId(openid);
                setWxUserInfo(profile,session.getRecvJson().getJSONObject("wxUserInfo"));
                profile.setRegisterTime(nowTime);
                service.addEntry(profile);
            }
            String unionId = wxLoginInfoJson.getString("unionId");
            if (!StringUtils.isNullOrEmpty(unionId))
                profile.setUnionId(unionId);
            addProfileInfo(channelId, profile);
        }else{
            //更新微信信息
            setWxUserInfo(profile,session.getRecvJson().getJSONObject("wxUserInfo"));
            service.addEntry(profile);
        }
        return profile;
    }

    private void setWxUserInfo(Profile profile, JSONObject wxUserInfo)
    {
        profile.setWxNickName(wxUserInfo.getString("nickName"));
        profile.setWxGender(wxUserInfo.getString("gender"));
        profile.setWxLanguage(wxUserInfo.getString("language"));
        profile.setWxCity(wxUserInfo.getString("city"));
        profile.setWxProvince(wxUserInfo.getString("province"));
        profile.setWxCountry(wxUserInfo.getString("country"));
        profile.setWxAvatarUrl(wxUserInfo.getString("avatarUrl"));
    }

    public Profile addProfileInfo(String channelId, Profile info)
    {
        return profileMap.put(channelId, info);
    }

    public Profile getProfileInfo(String channelId)
    {
        return profileMap.get(channelId);
    }

}
