# 使用官方OpenJDK 17基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制构建好的jar文件到容器中
COPY target/bed-dialoQ-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]