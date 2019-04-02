package com.zookeeper.stocks;

import org.apache.zookeeper.*;

import java.io.IOException;

public class StocksUpdater extends Thread implements Watcher {
    private static String hostPort = "localhost:2181";
    private static String stocksPath = "/stocks";
    private static String stocksIpcPath = "/stocks/ipc";
    private static String stocksSP500 = "/stocks/sp500";
    private static String stocksDowJone = "/stocks/DowJones";
    private static String stocksNasdaq = "/stocks/Nasdaq";
    ZooKeeper zk;

    public StocksUpdater() throws IOException {
        try {
            zk = new ZooKeeper(hostPort, 2000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        StocksUpdater dataUpdater = new StocksUpdater();
        try {
            while (true) {
                dataUpdater.execute();
                Thread.sleep(5000); // Sleep for 5 secs
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * The DataUpdater class updates the znode path /MyConfig every 5 seconds with a new UUID string.
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void execute() throws InterruptedException, KeeperException {
        try {
            while (true) {
                String path = getRandomIndex()[0].toString();
                String value = getRandomIndex()[1].toString();

                if (zk.exists(stocksPath, this) == null) {
                    zk.create(stocksPath, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }

                if (zk.exists(path, this) == null) {
                    zk.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                } else {
                    zk.setData(path, value.getBytes(), -1);
                }
                Thread.sleep(5000); // Sleep for 5 secs
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.printf("\nEvent Created/Updated: %s", event.toString());
    }

    public static Object[] getRandomIndex() {
        int min = 100;
        int max = 1000;
        int stockValue = (int) ((Math.random() * ((max - min) + 1)) + min);

        min = 1;
        max = 4;
        int index = (int) ((Math.random() * ((max - min) + 1)) + min);

        Object[] objects = new Object[2];

        switch (index) {
            case 1:
                objects[0] = stocksIpcPath;
                break;
            case 2:
                objects[0] = stocksSP500;
                break;
            case 3:
                objects[0] = stocksDowJone;
                break;
            case 4:
                objects[0] = stocksNasdaq;
                break;
        }
        objects[1] = stockValue;

        return objects;
    }
}
