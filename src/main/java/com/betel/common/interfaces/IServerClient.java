package com.betel.common.interfaces;

import com.betel.common.Monitor;
import com.betel.config.ServerConfigVo;

public interface IServerClient
{
    void start(ServerConfigVo srvCfg, final Monitor monitor);
}
