
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/interaction-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8008

ENTRYPOINT ["java", "-jar", "app.jar"]
