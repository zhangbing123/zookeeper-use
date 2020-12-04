package com.zb.zookeeper.use.distributed.watcher;

import com.zb.zookeeper.use.distributed.lock.LockClient;
import lombok.extern.log4j.Log4j2;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-12-01 16:40
 **/
@Log4j2
public class LockNodeWatchedEvent implements Watcher {

    private String rootPath;
    private String lock;
    private LockClient lockClient;

    public LockNodeWatchedEvent(String rootPath, String lock, LockClient lockClient) {
        this.rootPath = rootPath;
        this.lock = lock;
        this.lockClient = lockClient;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted) {

            log.info(watchedEvent.getPath() + "被删除....");
            try {
                CountDownLatch countDownLatch = lockClient.getCountDownLatch(watchedEvent.getPath());
                if (countDownLatch!=null && countDownLatch.getCount() > 0) {//在等待锁的队列中 中间的一个线程对应的节点被删除了，则线程退出等待 获取锁失败
                    lockClient.clearLockMap(watchedEvent.getPath());
                    countDownLatch.countDown();
                }
                lockClient.listenerLock(rootPath, lock);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
