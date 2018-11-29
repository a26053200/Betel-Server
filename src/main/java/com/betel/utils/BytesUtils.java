package com.betel.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.betel.consts.ServerConsts;
import io.netty.buffer.ByteBuf;

/**
 * @FileName: BytesUtils.java
 * @Package:utils
 * @Description: TODO
 * @author: zhengnan
 * @date:2018年5月8日 下午4:55:16
 */
public class BytesUtils
{
    //所有数据转换为字符串
    public static String readString(ByteBuf buff)
    {
        return readString(buff, buff.readableBytes());
    }

    public static String readString(ByteBuf buff, int len)
    {
        byte[] bytes = new byte[len];
        buff.readBytes(bytes);
        String res = readString(bytes);
        return res.trim();
    }

    public static String readString(byte[] bytes)
    {
        String res = new String(bytes, Charset.forName(ServerConsts.CHARSET_UTF_8));
        return res.trim();
    }

    public static byte[] string2Bytes(String str)
    {
        return string2Bytes(str, true);
    }

    public static byte[] string2Bytes(String str, boolean addEnd)
    {
        str = str + (addEnd ? "\0" : "");
        byte[] bytes = null;
        try
        {
            bytes = str.getBytes(ServerConsts.CHARSET_UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return bytes;
    }

    public static byte[] string2Bytes(String str, int length)
    {
        byte[] bytes = new byte[length];
        try
        {
            byte[] byteStr = str.getBytes(ServerConsts.CHARSET_UTF_8);
            System.arraycopy(byteStr, 0, bytes, 0, byteStr.length);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value)
    {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value)
    {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    //打包
    public static byte[] packBytes(byte[] bytes)
    {
        byte[] header = intToBytes2(bytes.length);
        byte[] allBytes = new byte[bytes.length + 4];
        System.arraycopy(header, 0, allBytes, 0, header.length);
        System.arraycopy(bytes, 0, allBytes, 4, bytes.length);
        return allBytes;
    }

    /**
     * long数组转化为byte数组
     *
     * @param longArray
     * @return
     * @throws IOException
     */
    public static byte[] longToByte(long[] longArray)
    {
        byte[] byteArray = new byte[longArray.length * 8];
        for (int i = 0; i < longArray.length; i++)
        {
            byteArray[0 + 8 * i] = (byte) (longArray[i] >> 56);
            byteArray[1 + 8 * i] = (byte) (longArray[i] >> 48);
            byteArray[2 + 8 * i] = (byte) (longArray[i] >> 40);
            byteArray[3 + 8 * i] = (byte) (longArray[i] >> 32);
            byteArray[4 + 8 * i] = (byte) (longArray[i] >> 24);
            byteArray[5 + 8 * i] = (byte) (longArray[i] >> 16);
            byteArray[6 + 8 * i] = (byte) (longArray[i] >> 8);
            byteArray[7 + 8 * i] = (byte) (longArray[i] >> 0);
        }
        return byteArray;
    }

    /**
     * byte数组转化为long数组
     *
     * @param byteArray
     * @return
     * @throws IOException
     */
    public static long[] byteToLong(byte[] byteArray)
    {
        long[] longArray = new long[byteArray.length / 8];
        for (int i = 0; i < longArray.length; i++)
        {
            longArray[i] = (((long) byteArray[0 + 8 * i] & 0xff) << 56)
                    | (((long) byteArray[1 + 8 * i] & 0xff) << 48)
                    | (((long) byteArray[2 + 8 * i] & 0xff) << 40)
                    | (((long) byteArray[3 + 8 * i] & 0xff) << 32)
                    | (((long) byteArray[4 + 8 * i] & 0xff) << 24)
                    | (((long) byteArray[5 + 8 * i] & 0xff) << 16)
                    | (((long) byteArray[6 + 8 * i] & 0xff) << 8)
                    | (((long) byteArray[7 + 8 * i] & 0xff) << 0);

        }
        return longArray;
    }

    public static int byteArrayToInt(byte[] b)
    {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a)
    {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
}
