package com.betel.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: DBUtils
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/7/31 22:40
 */
public class DBUtils
{
    private static final String CharSet = "ISO-8859-1";

    public static List<Long> String2Long(List<String> strList)
    {
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < strList.size(); i++)
        {
            list.add(Long.parseLong(strList.get(i)));
        }
        return list;
    }

    public static List<Integer> String2Int(List<String> strList)
    {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < strList.size(); i++)
        {
            list.add(Integer.parseInt(strList.get(i)));
        }
        return list;
    }

    //序列化
    public static String serializeToString(Object obj) throws Exception
    {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(obj);
        String str = byteOut.toString(CharSet);//此处只能是ISO-8859-1,但是不会影响中文使用
        return str;
    }

    //反序列化
    public static Object deserializeToObject(String str) throws Exception
    {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(str.getBytes(CharSet));
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        Object obj = objIn.readObject();
        return obj;
    }

}
