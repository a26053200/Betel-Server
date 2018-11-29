package com.betel.servers.business.modules.beans;

/**
 * @ClassName: Product
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/26 0:37
 */
public class Product
{
    private String id;
    private String name;            //名称
    private String catId;           //分类
    private String brandId;         //品牌
    private String specId;          //规格
    private String unit;            //商品单位
    private int number;             //库存
    private float price;            //商品价
    private String addTime;         //添加时间
    private String updateTime;      //更新时间

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

    public String getCatId()
    {
        return catId;
    }

    public void setCatId(String catId)
    {
        this.catId = catId;
    }

    public String getBrandId()
    {
        return brandId;
    }

    public void setBrandId(String brandId)
    {
        this.brandId = brandId;
    }

    public String getSpecId()
    {
        return specId;
    }

    public void setSpecId(String specId)
    {
        this.specId = specId;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public float getPrice()
    {
        return price;
    }

    public void setPrice(float price)
    {
        this.price = price;
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
