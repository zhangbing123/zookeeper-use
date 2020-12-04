package com.zb.zookeeper.use.distributed.report;

import com.alibaba.fastjson.JSON;
import com.zb.zookeeper.use.distributed.clustermanagement.ResouceUsage;
import lombok.extern.log4j.Log4j2;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-12-01 17:28
 **/
@Log4j2
public class ReportResource {


    private ZooKeeper zooKeeper;

    private String path;


    public ReportResource(ZooKeeper zooKeeper, String path) {
        this.zooKeeper = zooKeeper;
        this.path = path;
    }

    public void startReport(ResouceUsage resouceUsage) {
        log.info("start reporting resource information every 5 seconds");

        new Thread(() -> {

            Stat stat = new Stat();
            while (true) {
                try {
                    resouceUsage.setCup(resouceUsage.getCup() + 5);
                    resouceUsage.setMemo(resouceUsage.getMemo() + 8);
                    resouceUsage.setDisk(resouceUsage.getDisk() + 10);
                    log.info("server:{},上报资源信息", resouceUsage.getServerName());
                    zooKeeper.getData(path, false, stat);
                    zooKeeper.setData(path, JSON.toJSONString(resouceUsage).getBytes(), stat.getVersion());
                    Thread.currentThread().sleep(5000);//5s中上报一次
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
