package com.zb.zookeeper.use.distributed.lock;

import com.zb.zookeeper.use.distributed.watcher.LockNodeWatchedEvent;
import lombok.extern.log4j.Log4j2;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-12-03 14:16
 **/
@Log4j2
@Component
public class LockClient {

    @Autowired
    private ZooKeeper zooKeeper;

    private ConcurrentHashMap<String, CountDownLatch> lockMap = new ConcurrentHashMap();
    private ConcurrentHashMap<String, String> resultMap = new ConcurrentHashMap();

    public CountDownLatch getCountDownLatch(String path) {
        return lockMap.get(path);
    }

    public void clearLockMap(String path) {
        lockMap.remove(path);
    }

    public void clearResultMap(String path) {
        resultMap.remove(path);
    }

    public String getResult(String path) {
        String result = resultMap.get(path);
        clearResultMap(path);
        return result;
    }

    public String tryLock(String basePath, String lock) throws KeeperException, InterruptedException {

        basePath = "/" + basePath;

        //创建锁的根节点
        createLockRootIfNecessary(basePath);

        //创建一个临时顺序节点
        String lockPath = basePath.concat("/").concat(lock);
        if (resultMap.contains(Thread.currentThread().getId())) {
            //当前线程已创建了临时节点 并且线程当前并未阻塞说明已经获取了锁
            return resultMap.get(Thread.currentThread().getId());
        }
        String path = zooKeeper.create(lockPath, "write".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        //创建一个CountDownLatch
        CountDownLatch countDownLatch = createCountDownLatch(path);

        //监听上一个节点
        listenerLock(basePath, path);

        //等待线程获取到锁
        countDownLatch.await();

        return getResult(path);
    }

    public void unLock(String lock) {
        try {
            log.info(lock + ":释放锁");
            zooKeeper.delete(lock, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    private void createLockRootIfNecessary(String basePath) throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists(basePath, null);
        if (stat == null) {
            log.info("create lock root node to zookeeper，it is a persistent node ");
            try {
                zooKeeper.create(basePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (Exception e) {

            }
        }
    }

    private CountDownLatch createCountDownLatch(String path) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        lockMap.put(path, countDownLatch);
        return countDownLatch;
    }

    public void listenerLock(String basePath, String path) throws KeeperException, InterruptedException {

        CountDownLatch countDownLatch = getCountDownLatch(path);

        List<String> childrens = zooKeeper.getChildren(basePath, null);

        //根节点下的所有子节点排序
        Collections.sort(childrens);

        //当前节点的目录名
        String replace = path.replace(basePath.concat("/"), "");

        int index = childrens.indexOf(replace);

        //获取当前节点的上一个节点
        if (index > 0) {

            log.info(Thread.currentThread().getName() + "获取锁失败...等待");

//            Thread.sleep(40000);//睡眠锁40s

            int i = index - 1;
            String children = childrens.get(i);
            //监听上一个节点
            try {
                zooKeeper.getChildren(basePath.concat("/").concat(children), new LockNodeWatchedEvent(basePath, path, this));
            } catch (Exception e) {
                //监听异常继续向上监听
                listenerLock(basePath, path);
            }

        } else {//当前节点就是第一个  直接获取锁
            resultMap.put(path, path);
            clearLockMap(path);
            countDownLatch.countDown();
        }
    }


}
