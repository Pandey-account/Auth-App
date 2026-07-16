
# =========================
# Stage 1 - Build Stage
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests


# =========================
# Stage 2 - Runtime Stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy generated jar from build stage
COPY --from=builder /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# JVM optimizations for containers
ENTRYPOINT ["java", \
"-XX:+UseContainerSupport", \
"-XX:MaxRAMPercentage=75.0", \
"-jar", \
"app.jar"]
