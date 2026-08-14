# Sử dụng JDK 17 làm nền tảng
FROM openjdk:17-jdk-slim

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép file JAR đã build từ thư mục local vào container
COPY api/build/libs/api-0.0.1-SNAPSHOT.jar app.jar

# Thiết lập cổng mạng ứng dụng chạy (cổng mặc định của review-service là 8083)
EXPOSE 8083

# Khởi chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
