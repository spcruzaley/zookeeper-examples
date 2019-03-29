package com.zookeeper.chapter3.example5;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;

public class CuratorFrameworkClient {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181",
                new RetryOneTime(1));
        client.start();
        try {
            String path = client.create().withMode(CreateMode.PERSISTENT).forPath("/test_znode", "".getBytes());
            System.out.println("Path: " + path + " created...");
        } finally {
            client.close();
        }
    }

}
