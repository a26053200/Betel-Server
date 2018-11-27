package com.betel.servers.business.modules.beans;

/**
 * @ClassName: Spec
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/19 23:20
 */
public class Spec
{
    private String id;
    private String number;
    private String name;
    private String addTime;         //添加时间
    private String updateTime;      //添加时间

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddTime()
    {
        return addTime;
    }

    public void setAddTime(String addTime)
    {
        this.addTime = addTime;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
}
