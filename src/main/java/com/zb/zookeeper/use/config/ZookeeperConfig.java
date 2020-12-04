package com.zb.zookeeper.use.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-12-01 09:45
 **/
@Configuration
@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperConfig {

    @Value("${zookeeper.address}")
    private String address;

    @Value("${zookeeper.session.timeout}")
    private int sessionTimeout;

    @Value("${zookeeper.connection.timeout}")
    private int connectionTimeout;

    public String getAddress() {
        return address;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }
}
