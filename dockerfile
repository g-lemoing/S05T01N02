FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/S05T01N01-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]