# 🐘 Zookeeper Java Örnek Uygulaması

Bu proje, Apache Zookeeper'ın temel özelliklerini gösteren Java tabanlı bir örnek uygulamadır. Zookeeper'ın dağıtık sistemlerde koordinasyon, konfigürasyon yönetimi ve servis keşfi gibi önemli işlevlerini  barındırır.

## 📋 İçindekiler

- [Özellikler](#özellikler)
- [Gereksinimler](#gereksinimler)
- [Kurulum](#kurulum)
- [Kullanım](#kullanım)
- [Zookeeper Nedir?](#zookeeper-nedir)
- [Proje Yapısı](#proje-yapısı)
- [API Örnekleri](#api-örnekleri)
- [Katkıda Bulunma](#katkıda-bulunma)
- [Lisans](#lisans)

## ✨ Özellikler

- 🔧 **Node İşlemleri**: Oluşturma, okuma, güncelleme, silme
- 📁 **Hiyerarşik Yapı**: Tree-like node organizasyonu
- 👀 **Watcher Sistemi**: Gerçek zamanlı değişiklik izleme
- 🎯 **Demo Modu**: Otomatik örnek işlemler
- 🐳 **Docker Desteği**: Kolay kurulum ve çalıştırma
- 📊 **Logging**: Detaylı log kayıtları

## 🛠️ Gereksinimler

- **Java 8** veya üzeri
- **Maven 3.6** veya üzeri
- **Docker Desktop** (opsiyonel, Zookeeper sunucusu için)

## 🚀 Kurulum

### 1. Projeyi Klonlayın
```bash
git clone https://github.com/kullaniciadi/zookeeper-java-example.git
cd zookeeper-java-example
```

### 2. Zookeeper Sunucusunu Başlatın

#### Docker ile (Önerilen)
```bash
docker-compose up -d
```

#### Manuel Kurulum
Zookeeper'ı manuel olarak kurmak için [resmi dokümantasyonu](https://zookeeper.apache.org/doc/current/zookeeperStarted.html) takip edin.

### 3. Uygulamayı Çalıştırın

#### Windows
```bash
run.bat
```

#### Linux/Mac
```bash
mvn clean compile exec:java -Dexec.mainClass="com.zookeeper.example.ZookeeperApp"
```

## 📖 Kullanım

Uygulama başlatıldığında aşağıdaki menü görünecektir:

```
=== ZOOKEEPER MENÜ ===
1. Node Oluştur
2. Node Verisi Oku
3. Node Verisi Güncelle
4. Node Sil
5. Alt Node'ları Listele
6. Node Var Mı Kontrol Et
7. Node İstatistikleri
8. Demo Çalıştır
9. Çıkış
```

### Örnek Kullanım

1. **Node Oluşturma**: `/config/database` path'inde bir node oluşturun
2. **Veri Okuma**: Oluşturduğunuz node'un verisini okuyun
3. **Demo Çalıştırma**: Otomatik örnek işlemleri deneyin

## 🐘 Zookeeper Nedir?

Apache Zookeeper, dağıtık sistemler için açık kaynaklı bir koordinasyon servisidir. Büyük ölçekli uygulamalarda şu işlevleri sağlar:

### Ana Kullanım Alanları

- **Konfigürasyon Yönetimi**: Merkezi konfigürasyon saklama
- **Servis Keşfi**: Hangi servislerin nerede çalıştığını takip etme
- **Lider Seçimi**: Birden fazla sunucu arasında lider belirleme
- **Dağıtık Kilitleme**: Eşzamanlı erişim kontrolü
- **Hata Tespiti**: Servis sağlık durumu izleme

### Hiyerarşik Yapı Örneği
```
/
├── /config
│   ├── /database
│   │   ├── /url = "jdbc:mysql://localhost:3306/mydb"
│   │   └── /username = "admin"
│   └── /redis
│       └── /host = "redis-server:6379"
├── /services
│   ├── /user-service
│   │   ├── /instances/instance-1 = "192.168.1.10:8080"
│   │   └── /instances/instance-2 = "192.168.1.11:8080"
│   └── /order-service
└── /locks
    └── /database-write = "192.168.1.10:timestamp"
```

## 📁 Proje Yapısı

```
zookeeper-java-example/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/zookeeper/example/
│   │   │       ├── ZookeeperApp.java      # Ana uygulama
│   │   │       └── ZookeeperManager.java  # Zookeeper işlemleri
│   │   └── resources/
│   │       └── logback.xml               # Log konfigürasyonu
│   └── test/
│       └── java/
│           └── com/zookeeper/example/
│               └── ZookeeperManagerTest.java
├── docker-compose.yml                    # Docker konfigürasyonu
├── pom.xml                              # Maven konfigürasyonu
├── run.bat                              # Windows çalıştırma scripti
└── README.md                            # Bu dosya
```

## 🔧 API Örnekleri

### Node Oluşturma
```java
ZookeeperManager manager = new ZookeeperManager();
manager.connect();
manager.createNode("/config/database", "jdbc:mysql://localhost:3306/mydb");
```

### Node Verisi Okuma
```java
String data = manager.getNodeData("/config/database");
System.out.println("Database URL: " + data);
```

### Node Güncelleme
```java
manager.updateNodeData("/config/database", "jdbc:mysql://new-server:3306/mydb");
```

### Alt Node'ları Listeleme
```java
List<String> children = manager.getChildren("/config");
for (String child : children) {
    System.out.println("Child: " + child);
}
```

### Node Varlık Kontrolü
```java
boolean exists = manager.nodeExists("/config/database");
if (exists) {
    System.out.println("Node mevcut!");
}
```

## 🧪 Test

Testleri çalıştırmak için:

```bash
mvn test
```

## 🔍 Logging

Uygulama, `logback.xml` konfigürasyonu ile detaylı log kayıtları tutar. Loglar konsola ve dosyaya yazılır.

## 🐳 Docker

### Zookeeper Sunucusu
```bash
# Başlatma
docker-compose up -d

# Durumu kontrol etme
docker-compose ps

# Durdurma
docker-compose down
```

### Uygulama Container'ı (Gelecek)
```bash
# Build
docker build -t zookeeper-app .

# Çalıştırma
docker run --network zookeeper-network zookeeper-app
```


## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.


