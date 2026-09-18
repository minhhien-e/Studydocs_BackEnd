FROM eclipse-temurin:17-jdk-alpine AS build
RUN apk add --no-cache bash
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle gradle.properties ./
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
