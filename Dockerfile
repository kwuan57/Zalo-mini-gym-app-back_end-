# Bước 1: Build ứng dụng với Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Sao chép toàn bộ mã nguồn vào thư mục /app trong container
COPY . /app

# Chạy Maven để xây dựng ứng dụng, bỏ qua các bài kiểm tra
RUN mvn -f /app/pom.xml clean package -DskipTests

# Kiểm tra thư mục target trong container build để chắc chắn rằng tệp JAR đã được tạo ra
RUN ls -al /app/target

# Bước 2: Sử dụng image OpenJDK 17 (phù hợp với Spring Boot)
FROM eclipse-temurin:21.0.7_6-jdk

# Tạo thư mục làm việc trong container
WORKDIR /app

# Sao chép file JAR từ container build sang container hiện tại
COPY --from=build /app/target/gym-0.0.1-SNAPSHOT.jar app.jar

# Cổng mặc định của Spring Boot
EXPOSE 8080

# Lệnh khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
