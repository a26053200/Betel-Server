package com.betel.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.betel.consts.ServerConsts;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author zhengnan
 * @Date 2020/5/30
 */
public class JSONArrayUtils
{
    final static Logger logger = LogManager.getLogger(JSONArrayUtils.class);

    public static JSONObject getJsonObject(String filePath)
    {
        String content = null;
        try
        {
            logger.info("load json file:" + filePath);
            content = FileUtils.readFileToString(new File(filePath), ServerConsts.CHARSET_UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return JSON.parseObject(content);
    }

    public static List<String> getStringList(JSONArray array, String field)
    {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < array.size() ; i++)
        {
            list.add(array.getJSONObject(i).getString(field));
        }
        return list;
    }

    public static JSONArray getJSONArray(String file, String listField)
    {
        JSONObject json = getJsonObject(file);
        JSONArray array = json.getJSONArray(listField);
        return array;
    }

    public static <T> List<T> getDataList(String file, Class<T> clazz, String listField)
    {
        List<T> list = new ArrayList<>();
        JSONArray array = getJSONArray(file, listField);
        for (int i = 0; i < array.size(); i++)
        {
            list.add(i, array.getJSONObject(i).toJavaObject(clazz));
        }
        return list;
    }
}
