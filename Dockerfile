# Stage 1: build the Spring Boot jar
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests -DskipDocs

# Stage 2: build runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/target/thymeleaf-crud-1.0.0.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
