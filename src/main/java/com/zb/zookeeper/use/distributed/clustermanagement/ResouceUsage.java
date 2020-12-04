package com.zb.zookeeper.use.distributed.clustermanagement;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-12-01 15:30
 **/
public class ResouceUsage {

    private String serverName;

    private int cup;

    private int memo;

    private int disk;

    private String ip;

    public ResouceUsage() {
    }

    public ResouceUsage(String serverName, int cup, int memo, int disk, String ip) {
        this.serverName = serverName;
        this.cup = cup;
        this.memo = memo;
        this.disk = disk;
        this.ip = ip;
    }

    public String getServerName() {
        return serverName;
    }

    public int getCup() {
        return cup;
    }

    public int getMemo() {
        return memo;
    }

    public int getDisk() {
        return disk;
    }

    public String getIp() {
        return ip;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setCup(int cup) {
        this.cup = cup;
    }

    public void setMemo(int memo) {
        this.memo = memo;
    }

    public void setDisk(int disk) {
        this.disk = disk;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
