FROM openjdk:21-jdk-slim
EXPOSE 8030
ADD target/ofx-ms-data.jar ofx-ms-data.jar
ENTRYPOINT [ "java", "-jar", "/ofx-ms-data.jar" ]