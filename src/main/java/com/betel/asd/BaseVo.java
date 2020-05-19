package com.betel.asd;

import java.io.Serializable;

/**
 * @Description
 * @Author zhengnan
 * @Date 2020/5/19
 */
public class BaseVo implements Serializable
{
    private String id;
    private String vid;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getVid()
    {
        return vid;
    }

    public void setVid(String vid)
    {
        this.vid = vid;
    }
}
