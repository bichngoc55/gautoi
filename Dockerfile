FROM maven:3.9.3-eclipse-temurin-17-alpine as build
WORKDIR /build
COPY . .
RUN mvn clean package
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
