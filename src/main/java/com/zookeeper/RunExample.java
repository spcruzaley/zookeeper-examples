package com.zookeeper;

import com.zookeeper.chapter3.example1.HelloZooKeeper;
import com.zookeeper.chapter3.example2.DataUpdater;
import com.zookeeper.chapter3.example2.DataWatcher;
import com.zookeeper.chapter3.example3.ClusterClient;
import com.zookeeper.chapter3.example3.ClusterMonitor;
import com.zookeeper.chapter6.example4.MyCuratorClient;
import com.zookeeper.chapter6.example5.CuratorFrameworkClient;
import com.zookeeper.stocks.StocksUpdater;
import com.zookeeper.stocks.StocksWatcher;

public class RunExample {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Must have some example as argument...");
            howToUse();
        }
        switch (args[0]) {
            case "example1":
                HelloZooKeeper.main(args);
                break;
            case "example2-watcher":
                DataWatcher.main(args);
                break;
            case "example2-updater":
                DataUpdater.main(args);
                break;
            case "example3-monitor":
                ClusterMonitor.main(args);
                break;
            case "example3-client":
                ClusterClient.main(args);
                break;
            case "example4-curator-client":
                MyCuratorClient.main(args);
                break;
            case "example5-curator-framework":
                CuratorFrameworkClient.main(args);
                break;
            case "generate-stocks":
                StocksUpdater.main(args);
                break;
            case "watcher-stocks":
                StocksWatcher.main(args);
                break;
            default:
                System.out.println("Don't know how to do " + args[0]);
                howToUse();
        }
    }

    private static void howToUse() {
        System.out.println("-----------------------------------------\n" +
                "usage: zookeeper [EXAMPLE NUMBER]\n\n" +
                "examples availables:\n" +
                "\t[CHAPTER 3] example1\n" +
                "\t[CHAPTER 3] example2-watcher, example2-updater\n" +
                "\t[CHAPTER 3] example3-monitor <host:port>, example3-client <host:port>\n" +
                "\t[CHAPTER 6] example4-curator-client\n" +
                "\t[CHAPTER 6] example5-curator-framework\n" +
                "\t[Own Examples] generate-stocks, watcher-stocks\n" +
                "\nexample: zookeeper example2-updater\n");
        System.exit(0);
    }

}
