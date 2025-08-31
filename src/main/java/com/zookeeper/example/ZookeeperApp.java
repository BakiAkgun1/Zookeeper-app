package com.zookeeper.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

/**
 * Zookeeper örnek uygulaması
 */
public class ZookeeperApp {
    
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperApp.class);
    private static ZookeeperManager zookeeperManager;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        logger.info("=== Zookeeper Örnek Uygulaması Başlatılıyor ===");
        
        try {
            // Zookeeper manager'ı başlat
            zookeeperManager = new ZookeeperManager();
            scanner = new Scanner(System.in);
            
            // Zookeeper'a bağlan
            zookeeperManager.connect();
            
            // Ana menüyü göster
            showMainMenu();
            
        } catch (Exception e) {
            logger.error("Uygulama başlatılırken hata oluştu", e);
            System.err.println("Hata: " + e.getMessage());
        } finally {
            // Temizlik
            if (zookeeperManager != null) {
                zookeeperManager.disconnect();
            }
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    
    /**
     * Ana menüyü gösterir
     */
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== ZOOKEEPER MENÜ ===");
            System.out.println("1. Node Oluştur");
            System.out.println("2. Node Verisi Oku");
            System.out.println("3. Node Verisi Güncelle");
            System.out.println("4. Node Sil");
            System.out.println("5. Alt Node'ları Listele");
            System.out.println("6. Node Var Mı Kontrol Et");
            System.out.println("7. Node İstatistikleri");
            System.out.println("8. Demo Çalıştır");
            System.out.println("9. Çıkış");
            System.out.print("Seçiminizi yapın (1-9): ");
            
            String choice = scanner.nextLine().trim();
            
            try {
                switch (choice) {
                    case "1":
                        createNode();
                        break;
                    case "2":
                        readNodeData();
                        break;
                    case "3":
                        updateNodeData();
                        break;
                    case "4":
                        deleteNode();
                        break;
                    case "5":
                        listChildren();
                        break;
                    case "6":
                        checkNodeExists();
                        break;
                    case "7":
                        getNodeStats();
                        break;
                    case "8":
                        runDemo();
                        break;
                    case "9":
                        System.out.println("Uygulama kapatılıyor...");
                        return;
                    default:
                        System.out.println("Geçersiz seçim! Lütfen 1-9 arası bir sayı girin.");
                }
            } catch (Exception e) {
                logger.error("İşlem sırasında hata oluştu", e);
                System.err.println("Hata: " + e.getMessage());
            }
        }
    }
    
    /**
     * Node oluşturma işlemi
     */
    private static void createNode() throws Exception {
        System.out.print("Node path'i girin (örn: /test-node): ");
        String path = scanner.nextLine().trim();
        
        System.out.print("Node verisi girin: ");
        String data = scanner.nextLine().trim();
        
        zookeeperManager.createNode(path, data);
        System.out.println("✓ Node başarıyla oluşturuldu!");
    }
    
    /**
     * Node verisi okuma işlemi
     */
    private static void readNodeData() throws Exception {
        System.out.print("Okunacak node path'i girin: ");
        String path = scanner.nextLine().trim();
        
        String data = zookeeperManager.getNodeData(path);
        if (data != null) {
            System.out.println("✓ Node verisi: " + data);
        } else {
            System.out.println("✗ Node bulunamadı!");
        }
    }
    
    /**
     * Node verisi güncelleme işlemi
     */
    private static void updateNodeData() throws Exception {
        System.out.print("Güncellenecek node path'i girin: ");
        String path = scanner.nextLine().trim();
        
        System.out.print("Yeni veri girin: ");
        String newData = scanner.nextLine().trim();
        
        zookeeperManager.updateNodeData(path, newData);
        System.out.println("✓ Node verisi başarıyla güncellendi!");
    }
    
    /**
     * Node silme işlemi
     */
    private static void deleteNode() throws Exception {
        System.out.print("Silinecek node path'i girin: ");
        String path = scanner.nextLine().trim();
        
        zookeeperManager.deleteNode(path);
        System.out.println("✓ Node başarıyla silindi!");
    }
    
    /**
     * Alt node'ları listeleme işlemi
     */
    private static void listChildren() throws Exception {
        System.out.print("Alt node'ları listelenecek path'i girin: ");
        String path = scanner.nextLine().trim();
        
        List<String> children = zookeeperManager.getChildren(path);
        if (children != null && !children.isEmpty()) {
            System.out.println("✓ Alt node'lar:");
            for (String child : children) {
                System.out.println("  - " + child);
            }
        } else {
            System.out.println("✗ Alt node bulunamadı!");
        }
    }
    
    /**
     * Node varlık kontrolü
     */
    private static void checkNodeExists() throws Exception {
        System.out.print("Kontrol edilecek node path'i girin: ");
        String path = scanner.nextLine().trim();
        
        boolean exists = zookeeperManager.nodeExists(path);
        if (exists) {
            System.out.println("✓ Node mevcut!");
        } else {
            System.out.println("✗ Node mevcut değil!");
        }
    }
    
    /**
     * Node istatistikleri alma işlemi
     */
    private static void getNodeStats() throws Exception {
        System.out.print("İstatistikleri alınacak node path'i girin: ");
        String path = scanner.nextLine().trim();
        
        org.apache.zookeeper.data.Stat stat = zookeeperManager.getNodeStat(path);
        if (stat != null) {
            System.out.println("✓ Node İstatistikleri:");
            System.out.println("  - Version: " + stat.getVersion());
            System.out.println("  - Data Length: " + stat.getDataLength());
            System.out.println("  - Created: " + stat.getCtime());
            System.out.println("  - Modified: " + stat.getMtime());
        } else {
            System.out.println("✗ Node bulunamadı!");
        }
    }
    
    /**
     * Demo işlemleri çalıştırır
     */
    private static void runDemo() throws Exception {
        System.out.println("\n=== DEMO BAŞLATILIYOR ===");
        
        // Demo node'ları oluştur
        String demoPath = "/demo";
        String demoData = "Bu bir demo node'udur";
        
        System.out.println("1. Demo node oluşturuluyor...");
        zookeeperManager.createNode(demoPath, demoData);
        
        // Alt node'lar oluştur
        System.out.println("2. Alt node'lar oluşturuluyor...");
        zookeeperManager.createNode(demoPath + "/child1", "Alt node 1");
        zookeeperManager.createNode(demoPath + "/child2", "Alt node 2");
        zookeeperManager.createNode(demoPath + "/child3", "Alt node 3");
        
        // Node verisi oku
        System.out.println("3. Node verisi okunuyor...");
        String readData = zookeeperManager.getNodeData(demoPath);
        System.out.println("   Okunan veri: " + readData);
        
        // Alt node'ları listele
        System.out.println("4. Alt node'lar listeleniyor...");
        List<String> children = zookeeperManager.getChildren(demoPath);
        if (children != null) {
            for (String child : children) {
                System.out.println("   - " + child);
            }
        }
        
        // Node verisi güncelle
        System.out.println("5. Node verisi güncelleniyor...");
        zookeeperManager.updateNodeData(demoPath, "Güncellenmiş demo verisi");
        
        // İstatistikleri al
        System.out.println("6. Node istatistikleri alınıyor...");
        org.apache.zookeeper.data.Stat stat = zookeeperManager.getNodeStat(demoPath);
        if (stat != null) {
            System.out.println("   - Version: " + stat.getVersion());
            System.out.println("   - Data Length: " + stat.getDataLength());
        }
        
        // Temizlik
        System.out.println("7. Demo node'ları temizleniyor...");
        zookeeperManager.deleteNode(demoPath + "/child1");
        zookeeperManager.deleteNode(demoPath + "/child2");
        zookeeperManager.deleteNode(demoPath + "/child3");
        zookeeperManager.deleteNode(demoPath);
        
        System.out.println("✓ Demo tamamlandı!");
    }
}
