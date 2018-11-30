package com.betel.servers.business.modules.beans;

/**
 * @ClassName: Record
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/19 0:02
 */
public class Record
{
    private String id;
    private String type;            //记录类型
    private String content;         //记录内容
    private String recorderId;      //记录者OpenId
    private String addTime;         //记录添加时间

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getRecorderId()
    {
        return recorderId;
    }

    public void setRecorderId(String recorderId)
    {
        this.recorderId = recorderId;
    }

    public String getAddTime()
    {
        return addTime;
    }

    public void setAddTime(String addTime)
    {
        this.addTime = addTime;
    }
}
