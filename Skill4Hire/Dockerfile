# syntax=docker/dockerfile:1

# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder
WORKDIR /workspace

# Copy only what's needed for dependency resolution first (better caching)
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

# Now copy the source and build
COPY src ./src
RUN mvn -q clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

# Optional JVM tuning via JAVA_OPTS
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

# Expose Spring Boot default port (Render will set PORT env var at runtime)
EXPOSE 8080

# Copy the fat jar from the builder image (exclude the *.original)
ARG JAR_FILE=/workspace/target/*.jar
COPY --from=builder ${JAR_FILE} /app/app.jar

# Start the app. Bind to Render-provided $PORT or default to 8080 locally
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
