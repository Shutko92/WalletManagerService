# Build Stage
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Package Stage
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/*.jar /app.jar
EXPOSE 9001
CMD ["java", "-jar", "/app.jar"]