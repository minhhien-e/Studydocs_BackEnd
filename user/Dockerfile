# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Accept build arguments for GitHub Packages authentication
ARG GITHUB_ACTOR

# Set environment variables for Gradle to use
ENV GITHUB_ACTOR=${GITHUB_ACTOR}

# Copy Gradle wrapper and settings
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Copy all source code modules
COPY application application
COPY boot boot
COPY domain domain
COPY infrastructure infrastructure
COPY presentation presentation

# Make the wrapper executable and build the application using securely mounted secret
RUN --mount=type=secret,id=GITHUB_TOKEN \
    --mount=type=cache,target=/root/.gradle \
    export GITHUB_TOKEN=$(cat /run/secrets/GITHUB_TOKEN) && \
    sed -i 's/\r$//' gradlew && chmod +x gradlew && ./gradlew :boot:bootJar -x test

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create a non-root system group and user for security compliance
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the built jar from the builder stage and assign ownership to the spring user
COPY --from=builder --chown=spring:spring /app/boot/build/libs/boot-0.0.1-SNAPSHOT.jar app.jar

# Run the container as the non-root user
USER spring:spring

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
