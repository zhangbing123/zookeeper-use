//package com.zb.zookeeper.use.distributed.clustermanagement;
//
//import com.alibaba.fastjson.JSON;
//import com.zb.zookeeper.use.distributed.monitor.Monitor;
//import com.zb.zookeeper.use.distributed.report.ReportResource;
//import lombok.extern.log4j.Log4j2;
//import org.apache.zookeeper.CreateMode;
//import org.apache.zookeeper.KeeperException;
//import org.apache.zookeeper.ZooDefs;
//import org.apache.zookeeper.ZooKeeper;
//import org.apache.zookeeper.data.Stat;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//import static com.zb.zookeeper.use.distributed.Constants.CLUSTERMANAGE_ROOT;
//
///**
// * @description:
// * @author: zhangbing
// * @create: 2020-12-01 15:27
// **/
//@Log4j2
//@Service
//public class UserServer {
//
//    @Autowired
//    private ZooKeeper zooKeeper;
//    @Autowired
//    private Monitor monitor;
//
//    @PostConstruct
//    public void init() throws KeeperException, InterruptedException, UnknownHostException {
//
//        log.info("start init user server ....");
//        InetAddress address = InetAddress.getLocalHost();
//
//        ResouceUsage orderServer = new ResouceUsage("userServer", 20, 20, 20, address.getHostAddress());
//
//        Stat stat = zooKeeper.exists(CLUSTERMANAGE_ROOT, null);
//        if (stat == null) {
//            log.info("create cluster root node to zookeeper，it is a persistent node ");
//            zooKeeper.create(CLUSTERMANAGE_ROOT, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        }
//
//        log.info("create a ephemeral_sequential node of user server to zookeeper and report machine resource usage");
//        String path = CLUSTERMANAGE_ROOT + "/user";
//        String s = zooKeeper.create(path, JSON.toJSONString(orderServer).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
//
//        monitor.startMonitor(CLUSTERMANAGE_ROOT);
//
//        new ReportResource(zooKeeper, s).startReport(orderServer);
//
//    }
//
//}
