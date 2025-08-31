@echo off
echo ========================================
echo    Zookeeper Java Uygulaması
echo ========================================

echo.
echo 1. Docker Compose ile Zookeeper başlatılıyor...
docker-compose up -d

echo.
echo 2. Zookeeper'ın başlaması bekleniyor...
timeout /t 10 /nobreak > nul

echo.
echo 3. Zookeeper durumu kontrol ediliyor...
docker-compose ps

echo.
echo 4. Java uygulaması başlatılıyor...
echo.
mvn clean compile exec:java -Dexec.mainClass="com.zookeeper.example.ZookeeperApp"

echo.
echo ========================================
echo    Uygulama tamamlandı
echo ========================================
pause
