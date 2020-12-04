package com.zb.zookeeper.use.distributed.watcher;

import com.alibaba.fastjson.JSON;
import com.zb.zookeeper.use.distributed.clustermanagement.ResouceUsage;
import lombok.extern.log4j.Log4j2;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-12-01 16:43
 **/
@Log4j2
public class DataWatchedEvent implements Watcher {

    private ZooKeeper zookeeper;

    public DataWatchedEvent(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        switch (watchedEvent.getType()) {

            case DataWatchRemoved:
                log.info(watchedEvent.getPath() + "数据被移除");
                break;
            case NodeDataChanged:
                log.info(watchedEvent.getPath() + "数据被修改");
                processChangedData(watchedEvent);
                break;
        }


    }

    private void processChangedData(WatchedEvent watchedEvent) {
        try {
            byte[] data = zookeeper.getData(watchedEvent.getPath(), this, null);
            String s = new String(data);
            log.info(watchedEvent.getPath() + "的新数据:" + s);
            processData(s);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void processData(String data) {
        ResouceUsage resouceUsage = JSON.parseObject(data, ResouceUsage.class);
        if (resouceUsage != null) {
            if (resouceUsage.getCup() > 60) {
                log.info("ip:{},server:{},cpu资源占用过高:{}%", resouceUsage.getIp(), resouceUsage.getServerName(), resouceUsage.getCup());
            } else if (resouceUsage.getMemo() > 70) {
                log.info("ip:{},server:{},内存资源占用过高:{}%", resouceUsage.getIp(), resouceUsage.getServerName(), resouceUsage.getMemo());
            } else if (resouceUsage.getDisk() > 80) {
                log.info("ip:{},server:{},磁盘资源占用过高:{}%", resouceUsage.getIp(), resouceUsage.getServerName(), resouceUsage.getDisk());
            }
        }


    }
}
