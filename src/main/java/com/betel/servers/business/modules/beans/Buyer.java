package com.betel.servers.business.modules.beans;

/**
 * @ClassName: Buyer
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/18 23:28
 */
public class Buyer
{
    private String id;
    private String registerTime;    //买家第一次登陆游戏时间,即买家在该服务器的注册时间

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getRegisterTime()
    {
        return registerTime;
    }

    public void setRegisterTime(String registerTime)
    {
        this.registerTime = registerTime;
    }
}
