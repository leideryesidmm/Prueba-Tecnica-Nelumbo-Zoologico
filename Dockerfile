# Etapa de construcción usando Maven y JDK 17
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de ejecución usando JDK 17
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/api_zoologico-0.0.1-SNAPSHOT.war /app/api_zoologico-0.0.1-SNAPSHOT.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/api_zoologico-0.0.1-SNAPSHOT.war"]