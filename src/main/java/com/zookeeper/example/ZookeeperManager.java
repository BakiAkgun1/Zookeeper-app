package com.zookeeper.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Zookeeper işlemlerini yöneten sınıf
 */
public class ZookeeperManager implements Watcher {
    
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperManager.class);
    
    private ZooKeeper zooKeeper;
    private CountDownLatch connectedSignal = new CountDownLatch(1);
    
    private static final String ZOOKEEPER_HOST = "localhost:2181";
    private static final int SESSION_TIMEOUT = 5000;
    
    /**
     * Zookeeper'a bağlanır
     */
    public void connect() throws Exception {
        logger.info("Zookeeper'a bağlanılıyor: {}", ZOOKEEPER_HOST);
        
        zooKeeper = new ZooKeeper(ZOOKEEPER_HOST, SESSION_TIMEOUT, this);
        connectedSignal.await();
        
        logger.info("Zookeeper'a başarıyla bağlanıldı!");
    }
    
    /**
     * Zookeeper bağlantısını kapatır
     */
    public void disconnect() {
        try {
            if (zooKeeper != null) {
                zooKeeper.close();
                logger.info("Zookeeper bağlantısı kapatıldı.");
            }
        } catch (InterruptedException e) {
            logger.error("Zookeeper bağlantısı kapatılırken hata oluştu", e);
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Yeni bir node oluşturur
     */
    public void createNode(String path, String data) throws Exception {
        logger.info("Node oluşturuluyor: {} - Data: {}", path, data);
        
        try {
            String createdPath = zooKeeper.create(path, data.getBytes(), 
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info("Node başarıyla oluşturuldu: {}", createdPath);
        } catch (KeeperException.NodeExistsException e) {
            logger.warn("Node zaten mevcut: {}", path);
        } catch (Exception e) {
            logger.error("Node oluşturulurken hata oluştu: {}", path, e);
            throw e;
        }
    }
    
    /**
     * Node'un verisini okur
     */
    public String getNodeData(String path) throws Exception {
        logger.info("Node verisi okunuyor: {}", path);
        
        try {
            byte[] data = zooKeeper.getData(path, false, null);
            String nodeData = new String(data);
            logger.info("Node verisi okundu: {} - Data: {}", path, nodeData);
            return nodeData;
        } catch (KeeperException.NoNodeException e) {
            logger.warn("Node bulunamadı: {}", path);
            return null;
        } catch (Exception e) {
            logger.error("Node verisi okunurken hata oluştu: {}", path, e);
            throw e;
        }
    }
    
    /**
     * Node'un verisini günceller
     */
    public void updateNodeData(String path, String newData) throws Exception {
        logger.info("Node verisi güncelleniyor: {} - Yeni Data: {}", path, newData);
        
        try {
            Stat stat = zooKeeper.exists(path, false);
            if (stat != null) {
                zooKeeper.setData(path, newData.getBytes(), stat.getVersion());
                logger.info("Node verisi başarıyla güncellendi: {}", path);
            } else {
                logger.warn("Güncellenecek node bulunamadı: {}", path);
            }
        } catch (Exception e) {
            logger.error("Node verisi güncellenirken hata oluştu: {}", path, e);
            throw e;
        }
    }
    
    /**
     * Node'u siler
     */
    public void deleteNode(String path) throws Exception {
        logger.info("Node siliniyor: {}", path);
        
        try {
            Stat stat = zooKeeper.exists(path, false);
            if (stat != null) {
                zooKeeper.delete(path, stat.getVersion());
                logger.info("Node başarıyla silindi: {}", path);
            } else {
                logger.warn("Silinecek node bulunamadı: {}", path);
            }
        } catch (Exception e) {
            logger.error("Node silinirken hata oluştu: {}", path, e);
            throw e;
        }
    }
    
    /**
     * Node'un alt node'larını listeler
     */
    public List<String> getChildren(String path) throws Exception {
        logger.info("Alt node'lar listeleniyor: {}", path);
        
        try {
            List<String> children = zooKeeper.getChildren(path, false);
            logger.info("Alt node'lar bulundu: {} - Count: {}", path, children.size());
            return children;
        } catch (KeeperException.NoNodeException e) {
            logger.warn("Node bulunamadı: {}", path);
            return null;
        } catch (Exception e) {
            logger.error("Alt node'lar listelenirken hata oluştu: {}", path, e);
            throw e;
        }
    }
    
    /**
     * Node'un var olup olmadığını kontrol eder
     */
    public boolean nodeExists(String path) throws Exception {
        try {
            Stat stat = zooKeeper.exists(path, false);
            return stat != null;
        } catch (Exception e) {
            logger.error("Node kontrolü yapılırken hata oluştu: {}", path, e);
            throw e;
        }
    }
    
    /**
     * Node'un istatistiklerini alır
     */
    public Stat getNodeStat(String path) throws Exception {
        logger.info("Node istatistikleri alınıyor: {}", path);
        
        try {
            Stat stat = zooKeeper.exists(path, false);
            if (stat != null) {
                logger.info("Node istatistikleri alındı: {} - Version: {}, DataLength: {}", 
                    path, stat.getVersion(), stat.getDataLength());
            }
            return stat;
        } catch (Exception e) {
            logger.error("Node istatistikleri alınırken hata oluştu: {}", path, e);
            throw e;
        }
    }
    
    /**
     * Watcher event'ini işler
     */
    @Override
    public void process(WatchedEvent event) {
        logger.info("Zookeeper event alındı: {} - State: {}", event.getType(), event.getState());
        
        if (event.getState() == Event.KeeperState.SyncConnected) {
            connectedSignal.countDown();
        }
    }
    
    /**
     * Zookeeper bağlantısının durumunu kontrol eder
     */
    public boolean isConnected() {
        return zooKeeper != null && zooKeeper.getState() == ZooKeeper.States.CONNECTED;
    }
}
