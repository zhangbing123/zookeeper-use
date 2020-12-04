package com.zb.zookeeper.use.distributed.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-12-01 16:40
 **/
public class NodeWatchedEvent implements Watcher {

    private ZooKeeper zookeeper;
    private String rootPath;

    public NodeWatchedEvent(ZooKeeper zookeeper, String rootPath) {
        this.zookeeper = zookeeper;
        this.rootPath = rootPath;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.ChildWatchRemoved) {
            System.out.println(watchedEvent.getPath() + "节点被移除");
        } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            System.out.println(watchedEvent.getPath() + "节点新增");
        }
        try {
            zookeeper.getChildren(rootPath, this);

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
