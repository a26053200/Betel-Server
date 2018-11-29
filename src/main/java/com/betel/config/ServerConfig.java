package com.betel.config;

import com.betel.consts.ServerName;

import java.util.HashMap;

/**
 * @ClassName: ServerConfig
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/9/13 1:09
 */
public class ServerConfig
{
    private static HashMap<String, ServerConfigVo> s_cfgMap;

    /**
     * 获取服务器配置
     * @param serverName
     * @return
     */
    public static ServerConfigVo getServerConfig(String serverName)
    {
        if(s_cfgMap == null)
        {
            s_cfgMap = new HashMap<>();

            /** 网关服务器配置 **/
            s_cfgMap.put(ServerName.GATE_SERVER,
                    new ServerConfigVo(ServerName.GATE_SERVER,
                            "127.0.0.1",8090, //服务器地址
                            "127.0.0.1",6379));//数据库地址

            /** 均衡服务器配置 **/
            s_cfgMap.put(ServerName.BALANCE_SERVER,
                    new ServerConfigVo(ServerName.BALANCE_SERVER,
                            "127.0.0.1",8098, //服务器地址
                            "127.0.0.1",6379));//数据库地址

            /** 业务服务器配置 **/
            s_cfgMap.put(ServerName.BUSINESS_SERVER,
                    new ServerConfigVo(ServerName.BUSINESS_SERVER,
                            "127.0.0.1",8091, //服务器地址
                            "127.0.0.1",6379));//数据库地址
        }
        return s_cfgMap.get(serverName);
    }
}
