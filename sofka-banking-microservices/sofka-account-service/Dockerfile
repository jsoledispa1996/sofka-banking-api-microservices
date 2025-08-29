# Dockerfile para sofka-account-service
FROM maven:3.8.7-eclipse-temurin-11 AS builder
WORKDIR /build
COPY . /build
RUN mvn clean package -DskipTests -pl sofka-account-service -am

FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=builder /build/sofka-account-service/target/sofka-account-service-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
