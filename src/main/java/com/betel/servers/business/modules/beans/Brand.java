package com.betel.servers.business.modules.beans;

/**
 * @ClassName: Brand
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/18 21:26
 */
public class Brand
{
    private String id;
    private String name;
    private String addTime;    //添加时间
    private String updateTime;    //添加时间

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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
