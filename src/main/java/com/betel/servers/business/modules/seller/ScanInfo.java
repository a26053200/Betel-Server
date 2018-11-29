package com.betel.servers.business.modules.seller;

/**
 * @ClassName: ScanInfo
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/14 0:20
 */
public class ScanInfo
{
    private String state;
    private long startTime;
    private String scanProfileId;

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public String getScanProfileId()
    {
        return scanProfileId;
    }

    public void setScanProfileId(String scanProfileId)
    {
        this.scanProfileId = scanProfileId;
    }

    public ScanInfo(String state, long startTime)
    {
        this.state = state;
        this.startTime = startTime;
    }
}
