package com.zookeeper.chapter3.example1;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

public class HelloZooKeeper {

    public static void main(String[] args) throws IOException {
        String host = "localhost";
        String zpath = "/";
        List<String> zooChildren;

        ZooKeeper zk = new ZooKeeper(host, 2000, null);

        if (zk != null) {
            try {
                zooChildren = zk.getChildren(zpath, false);
                System.out.println("Znodes of '/': ");
                for (String child : zooChildren) {
                    //print the children
                    System.out.println(child);
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
