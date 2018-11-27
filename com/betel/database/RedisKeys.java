package com.betel.database;

/**
 * @ClassName: RedisKeys
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/18 23:07
 */
public class RedisKeys
{
    public static final String NOME                     = "";
    public static final String SPLIT                    = ":";
    public static final String WILDCARD                 = "*";
    public static final String ID                       = "id";//主键
    public static final String VID                      = "vid";//附键
    /**
     * 用户profile
     */
    public static final String profile                  = "profile";
    public static final String profile_id               = "profile_id";
    public static final String profile_open_id          = "profile_open_id";
    public static final String profile_union_id         = "profile_union_id";
    public static final String profile_register_time    = "profile_register_time";
    // 买家微信信息
    public static final String profile_wx_nickname      = "profile_wx_nickname";
    public static final String profile_wx_gender        = "profile_wx_gender";
    public static final String profile_wx_language      = "profile_wx_language";
    public static final String profile_wx_city          = "profile_wx_city";
    public static final String profile_wx_province      = "profile_wx_province";
    public static final String profile_wx_country       = "profile_wx_country";
    public static final String profile_wx_avatar_url    = "profile_wx_avatar_url";

    /**
     * 买家
     */
    public static final String buyer                    = "buyer";
    public static final String buyer_id                 = "buyer_id";
    public static final String buyer_profile_id         = "buyer_profile_id";
    public static final String buyer_register_time      = "buyer_register_time";

    /**
     * 商品
     */
    public static final String product                  = "product";
    public static final String product_id               = "record_id";
    public static final String product_add_time         = "product_add_time";
    public static final String product_update_time      = "record_update_time";

    /**
     * 商品品类
     */
    public static final String category                 = "category";
    public static final String category_id              = "category_id";
    public static final String category_name            = "category_name";
    public static final String category_add_time        = "category_add_time";
    public static final String category_update_time     = "category_update_time";

    /**
     * 商品品牌
     */
    public static final String brand                    = "brand";
    public static final String brand_id                 = "brand_id";
    public static final String brand_name               = "brand_name";
    public static final String brand_add_time           = "brand_add_time";
    public static final String brand_update_time        = "brand_update_time";

    /**
     * 商品规格
     */
    public static final String spec                     = "spec";
    public static final String spec_id                  = "spec_id";
    public static final String spec_name                = "spec_name";
    public static final String spec_add_time            = "spec_add_time";
    public static final String spec_update_time         = "spec_update_time";

    /**
     * 记录
     */
    public static final String record                   = "record";
    public static final String record_id                = "record_id";
    public static final String record_profile_id        = "record_profile_id";
    public static final String record_type              = "record_type";
    public static final String record_content           = "record_content";
    public static final String record_add_time          = "record_add_time";
}
