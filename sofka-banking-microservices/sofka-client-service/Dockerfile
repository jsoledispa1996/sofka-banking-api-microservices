# Dockerfile para sofka-client-service
FROM maven:3.8.7-eclipse-temurin-11 AS builder
WORKDIR /build
COPY . /build
RUN mvn clean package -DskipTests -pl sofka-client-service -am

FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=builder /build/sofka-client-service/target/sofka-client-service-1.0.0.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
