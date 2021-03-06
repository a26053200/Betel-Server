package com.betel.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: JsonUtils
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/6/1 0:55
 */
public class JsonUtils
{

    public static <T> JSONObject object2Json(T t)
    {
        JSONObject json = (JSONObject)JSONObject.toJSON(t);
        return json;
    }

    public static JSONObject map2jsonObject(Map<String, String> map)
    {
        JSONObject json = new JSONObject();
        for (String key : map.keySet()) {
            json.put(key,map.get(key));
        }
        return json;
    }

    public static <T> T map2Object(Map<String, String> map, Class<T> clazz)
    {
        JSONObject json = JsonUtils.map2jsonObject(map);
        T t = JSONObject.toJavaObject(json,clazz);
        return t;
    }

    public static String trim(String json)
    {
        return json.trim();
    }

    private static String getLevelStr(int level)
    {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++)
        {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

    public static String format(String json)
    {
        int level = 0;
        //存放格式化的json字符串
        StringBuffer jsonForMatStr = new StringBuffer();
        for (int index = 0; index < json.length(); index++)//将字符串中的字符逐个按行输出
        {
            //获取s中的每个字符
            char c = json.charAt(index);
//          System.out.println(s.charAt(index));

            //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1))
            {
                jsonForMatStr.append(getLevelStr(level));
//                System.out.println("123"+jsonForMatStr);
            }
            //遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c)
            {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }

    public static JSONArray list2jsonArray(List<String> list)
    {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++)
            jsonArray.add(list.get(i));
        return jsonArray;
    }

}