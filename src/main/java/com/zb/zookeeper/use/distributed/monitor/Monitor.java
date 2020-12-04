package com.zb.zookeeper.use.distributed.monitor;

import com.zb.zookeeper.use.distributed.watcher.DataWatchedEvent;
import com.zb.zookeeper.use.distributed.watcher.NodeWatchedEvent;
import lombok.extern.log4j.Log4j2;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-12-01 15:43
 **/
@Log4j2
@Component
public class Monitor {

    @Autowired
    private ZooKeeper zooKeeper;

    public void startMonitor(String path) throws KeeperException, InterruptedException {
        log.info("Start resource monitoring system");

        //监控节点变化
        List<String> children = zooKeeper.getChildren(path, new NodeWatchedEvent(zooKeeper, path));

        //监控节点下子节点的数据变化
        if (!CollectionUtils.isEmpty(children)) {
            DataWatchedEvent dataWatchedEvent = new DataWatchedEvent(zooKeeper);
            for (String child : children) {
                log.info("获取子节点：{}，数据", child);
                Stat stat = new Stat();
                byte[] data = zooKeeper.getData(path + "/" + child, dataWatchedEvent, stat);
                dataWatchedEvent.processData(new String(data));

            }
        }

    }

}
