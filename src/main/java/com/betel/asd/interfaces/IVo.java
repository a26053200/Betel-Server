package com.betel.asd.interfaces;

import java.io.Serializable;

/**
 * @Description
 * @Author zhengnan
 * @Date 2020/5/30
 */
public interface IVo extends Serializable
{
    String getId();

    void setId(String id);

    String getVid();

    void setVid(String vid);
}
