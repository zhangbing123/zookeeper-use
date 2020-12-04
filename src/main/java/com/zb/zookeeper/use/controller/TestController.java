package com.zb.zookeeper.use.controller;

import com.zb.zookeeper.use.distributed.lock.LockClient;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-12-03 15:37
 **/
@RequestMapping(value = "/test")
@RestController
public class TestController {

    @Autowired
    private LockClient lockClient;

    final int[] sum = {0};


    @GetMapping("/getLock")
    public void getLock(int count) {


        for (int i = 0; i < count; i++) {

            new Thread(() -> {
                try {
                    String lock = lockClient.tryLock("product2", Thread.currentThread().getName());
                    if (lock != null) {
                        System.out.println(Thread.currentThread().getName() + "获取锁");
                        System.out.println("数量增加：" + sum[0]++);
                        lockClient.unLock(lock);

                    } else {
                        System.out.println(Thread.currentThread().getName() + "退出竞争锁队列");
                    }


                } catch (KeeperException e) {


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "thread-" + i + ":").start();

        }


    }
}
