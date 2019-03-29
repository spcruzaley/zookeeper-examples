package com.zookeeper.chapter3.example4;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

public class MyCuratorClient {

    public static void main(String[] args) throws Exception {
        CuratorZookeeperClient client = new CuratorZookeeperClient("localhost:2181", 10000,
                10000, null, new RetryOneTime(1));
        client.start();
        try {
            client.blockUntilConnectedOrTimedOut();
            String path = client.getZooKeeper().create("/test_znode", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
            System.out.println("Path: " + path + " created...");
        } finally {
            client.close();
        }
    }

}
