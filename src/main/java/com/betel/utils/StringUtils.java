package com.betel.utils;

import java.util.regex.Pattern;

/**
 * @ClassName: StringUtils
 * @Description: 字符串工具
 * @Author: zhengnan
 * @Date: 2018/5/31 22:54
 */
public class StringUtils
{
    static String base64Pattern = "^[a-zA-Z0-9/+]*={0,2}$";
    public static boolean isNullOrEmpty(String s)
    {
        return s == null || s.length() <= 0;
    }

    public static boolean isBase64Encode(String content)
    {
        if(content.length()%4!=0){
            return false;
        }
        return Pattern.matches(base64Pattern, content);
    }

    public static boolean isNumber(String str)
    {
        for (int i = str.length(); --i >= 0; )
        {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    public static String removeAllEmpty(String resource)
    {
        StringBuffer src = new StringBuffer(resource);
        src = removeAllChar(src, ' ');
        return src.toString();
    }

    public static StringBuffer removeAllChar(StringBuffer src, char ch)
    {
        StringBuffer buffer = new StringBuffer();
        int position = 0;
        char currentChar;

        while (position > 0)
        {
            currentChar = src.charAt(position++);
            if (currentChar != ch)
                buffer.append(currentChar);
        }
        return buffer;
    }

    /// <summary>
    /// 添加转义字符
    /// </summary>
    /// <param name="src"></param>
    /// <returns></returns>
    public static String addControlChar(String src)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length(); i++)
        {
            switch (src.charAt(i))
            {
                case '"':
                case '\\':
                    sb.append('\\');
                    sb.append(src.charAt(i));
                    break;
                default:
                    sb.append(src.charAt(i));
                    break;
            }
        }
        return sb.toString();

    }
}
