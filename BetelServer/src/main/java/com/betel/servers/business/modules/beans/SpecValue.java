package com.betel.servers.business.modules.beans;

/**
 * @ClassName: SpecValue
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/19 23:48
 */
public class SpecValue
{
    private String id;
    private String specId;
    private String value;
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

    public String getSpecId()
    {
        return specId;
    }

    public void setSpecId(String specId)
    {
        this.specId = specId;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
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
