package com.zookeeper.stocks;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

public class StocksWatcher implements Watcher, Runnable {
    private static String hostPort = "localhost:2181";
    private static String stocksPath = "/stocks";
    private static String stocksIpcPath = "/stocks/ipc";
    private static String stocksSP500 = "/stocks/sp500";
    private static String stocksDowJones = "/stocks/DowJones";
    private static String stocksNasdaq = "/stocks/Nasdaq";
    byte zoo_data[] = null;
    ZooKeeper zk;

    public StocksWatcher() {
        try {
            zk = new ZooKeeper(hostPort, 2000, this);
            if (zk != null) {
                try {
                    //Create the znode if it doesn't exist, with the following code:
                    if (zk.exists(stocksPath, this) == null) {
                        zk.create(stocksPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }
                    if (zk.exists(stocksIpcPath, this) == null) {
                        zk.create(stocksIpcPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }
                    if (zk.exists(stocksSP500, this) == null) {
                        zk.create(stocksSP500, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }
                    if (zk.exists(stocksDowJones, this) == null) {
                        zk.create(stocksDowJones, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }
                    if (zk.exists(stocksNasdaq, this) == null) {
                        zk.create(stocksNasdaq, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, KeeperException {
        StocksWatcher dataWatcher = new StocksWatcher();
        dataWatcher.printData();
        dataWatcher.run();
    }

    /**
     *  Observe carefully here that when we perform a getData call on the znode, we set a watch again, which is
     *  the second parameter of the method.
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void printData() throws InterruptedException, KeeperException {
        //zoo_data = zk.getData(stocksPath, this, null);
        List<String> l = zk.getChildren(stocksPath, this, null);

        for (String str: l) {
            zoo_data = zk.getData(stocksPath + "/" + str, this, null);
            String zString = new String(zoo_data);
            System.out.printf("\nCurrent Data: %s - %s", str, zString);
        }

    }

    public void run() {
        try {
            synchronized (this) {
                while (true) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.printf("\nEvent Received: %s", event.toString()+"\n");

        //We will process only events of type NodeDataChanged
        if (event.getType() == Event.EventType.NodeDataChanged) {
            try {
                printData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n-----------------------------------------------------------");
    }
}
