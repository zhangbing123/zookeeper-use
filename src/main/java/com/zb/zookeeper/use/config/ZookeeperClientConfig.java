package com.zb.zookeeper.use.config;

import lombok.extern.log4j.Log4j2;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-12-01 14:44
 **/
@Log4j2
@Configuration
public class ZookeeperClientConfig {

    @Autowired
    private ZookeeperConfig zkConfig;

    @Bean
    public ZooKeeper zooKeeper() throws IOException {

        ZooKeeper zooKeeper = new ZooKeeper(zkConfig.getAddress(), zkConfig.getSessionTimeout(), watchedEvent -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected && watchedEvent.getType() == Watcher.Event.EventType.None) {
                log.info("connected zookeeper successfully!!!");
            }
        });
        return zooKeeper;

    }

}
