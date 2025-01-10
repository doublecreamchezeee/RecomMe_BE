FROM gradle:7.6-jdk17 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

COPY src ./src

RUN gradle build -x test --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar ./app.jar
COPY src/main/resources/.env ./
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
