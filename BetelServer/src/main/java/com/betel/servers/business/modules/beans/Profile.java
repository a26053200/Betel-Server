package com.betel.servers.business.modules.beans;

/**
 * @ClassName: Profile
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/18 23:35
 */
public class Profile
{
    private String id;              //用微信的openid做id
    //private String openId;
    private String unionId;
    private String registerTime;    //买家第一次登陆游戏时间,即买家在该服务器的注册时间
    // 微信用户信息
    private String wxNickName;
    private String wxGender;
    private String wxLanguage;
    private String wxCity;
    private String wxProvince;
    private String wxCountry;
    private String wxAvatarUrl;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUnionId()
    {
        return unionId;
    }

    public void setUnionId(String unionId)
    {
        this.unionId = unionId;
    }

    public String getRegisterTime()
    {
        return registerTime;
    }

    public void setRegisterTime(String registerTime)
    {
        this.registerTime = registerTime;
    }

    public String getWxNickName()
    {
        return wxNickName;
    }

    public void setWxNickName(String wxNickName)
    {
        this.wxNickName = wxNickName;
    }

    public String getWxGender()
    {
        return wxGender;
    }

    public void setWxGender(String wxGender)
    {
        this.wxGender = wxGender;
    }

    public String getWxLanguage()
    {
        return wxLanguage;
    }

    public void setWxLanguage(String wxLanguage)
    {
        this.wxLanguage = wxLanguage;
    }

    public String getWxCity()
    {
        return wxCity;
    }

    public void setWxCity(String wxCity)
    {
        this.wxCity = wxCity;
    }

    public String getWxProvince()
    {
        return wxProvince;
    }

    public void setWxProvince(String wxProvince)
    {
        this.wxProvince = wxProvince;
    }

    public String getWxCountry()
    {
        return wxCountry;
    }

    public void setWxCountry(String wxCountry)
    {
        this.wxCountry = wxCountry;
    }

    public String getWxAvatarUrl()
    {
        return wxAvatarUrl;
    }

    public void setWxAvatarUrl(String wxAvatarUrl)
    {
        this.wxAvatarUrl = wxAvatarUrl;
    }
}
