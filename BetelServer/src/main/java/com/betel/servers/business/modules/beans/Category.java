package com.betel.servers.business.modules.beans;

/**
 * @ClassName: Category
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/19 23:00
 */
public class Category
{
    private String id;
    private String pid;         //父级分类ID
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

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
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
