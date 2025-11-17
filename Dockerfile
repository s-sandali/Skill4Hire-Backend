# syntax=docker/dockerfile:1

# ----------------------------
# Build stage: compile the Spring Boot application
# ----------------------------
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder
WORKDIR /workspace

# Copy the backend project sources (cached if unchanged)
COPY Skill4Hire/ ./

# Build the application without running tests
RUN mvn -B -ntp clean package -DskipTests

# ----------------------------
# Runtime stage: lightweight JRE image
# ----------------------------
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

# Tuneable JVM options
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

# Expose the Spring Boot port (overridable via $PORT)
EXPOSE 8080

# Copy the built fat JAR from the builder image
COPY --from=builder /workspace/target/*.jar /app/app.jar

# Run the application (binds to $PORT if provided, otherwise 8080)
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
