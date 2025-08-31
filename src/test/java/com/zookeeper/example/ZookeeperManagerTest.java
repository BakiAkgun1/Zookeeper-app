package com.zookeeper.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * ZookeeperManager test sınıfı
 * Not: Bu testler Zookeeper'ın çalışır durumda olmasını gerektirir
 */
public class ZookeeperManagerTest {
    
    private ZookeeperManager zookeeperManager;
    
    @Before
    public void setUp() throws Exception {
        zookeeperManager = new ZookeeperManager();
        zookeeperManager.connect();
    }
    
    @After
    public void tearDown() {
        if (zookeeperManager != null) {
            zookeeperManager.disconnect();
        }
    }
    
    @Test
    public void testConnection() {
        assertTrue("Zookeeper bağlantısı başarısız", zookeeperManager.isConnected());
    }
    
    @Test
    public void testCreateAndReadNode() throws Exception {
        String testPath = "/test-node-" + System.currentTimeMillis();
        String testData = "test-data";
        
        // Node oluştur
        zookeeperManager.createNode(testPath, testData);
        
        // Node'un var olduğunu kontrol et
        assertTrue("Node oluşturulamadı", zookeeperManager.nodeExists(testPath));
        
        // Node verisini oku
        String readData = zookeeperManager.getNodeData(testPath);
        assertEquals("Node verisi yanlış okundu", testData, readData);
        
        // Temizlik
        zookeeperManager.deleteNode(testPath);
    }
    
    @Test
    public void testUpdateNode() throws Exception {
        String testPath = "/test-update-" + System.currentTimeMillis();
        String originalData = "original-data";
        String updatedData = "updated-data";
        
        // Node oluştur
        zookeeperManager.createNode(testPath, originalData);
        
        // Node'u güncelle
        zookeeperManager.updateNodeData(testPath, updatedData);
        
        // Güncellenmiş veriyi kontrol et
        String readData = zookeeperManager.getNodeData(testPath);
        assertEquals("Node güncellenemedi", updatedData, readData);
        
        // Temizlik
        zookeeperManager.deleteNode(testPath);
    }
    
    @Test
    public void testCreateChildren() throws Exception {
        String parentPath = "/test-parent-" + System.currentTimeMillis();
        String child1Path = parentPath + "/child1";
        String child2Path = parentPath + "/child2";
        
        // Parent node oluştur
        zookeeperManager.createNode(parentPath, "parent-data");
        
        // Child node'lar oluştur
        zookeeperManager.createNode(child1Path, "child1-data");
        zookeeperManager.createNode(child2Path, "child2-data");
        
        // Child node'ları listele
        List<String> children = zookeeperManager.getChildren(parentPath);
        assertNotNull("Child listesi null", children);
        assertEquals("Child sayısı yanlış", 2, children.size());
        assertTrue("Child1 bulunamadı", children.contains("child1"));
        assertTrue("Child2 bulunamadı", children.contains("child2"));
        
        // Temizlik
        zookeeperManager.deleteNode(child1Path);
        zookeeperManager.deleteNode(child2Path);
        zookeeperManager.deleteNode(parentPath);
    }
    
    @Test
    public void testNodeExists() throws Exception {
        String testPath = "/test-exists-" + System.currentTimeMillis();
        String nonExistentPath = "/non-existent-path";
        
        // Node oluştur
        zookeeperManager.createNode(testPath, "test-data");
        
        // Var olan node'u kontrol et
        assertTrue("Var olan node bulunamadı", zookeeperManager.nodeExists(testPath));
        
        // Var olmayan node'u kontrol et
        assertFalse("Var olmayan node bulundu", zookeeperManager.nodeExists(nonExistentPath));
        
        // Temizlik
        zookeeperManager.deleteNode(testPath);
    }
}
