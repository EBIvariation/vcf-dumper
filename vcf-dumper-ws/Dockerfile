# Build with the repository root as the build context:
#   docker build -f vcf-dumper-ws/Dockerfile -t vcf-dumper-ws .

# Build stage - Build without embedding credentials
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the full project
COPY . .

RUN mvn -B -pl vcf-dumper-ws -am clean package -DskipTests -Dmaven.gitcommitid.skip=true

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Create non-root user for security
RUN groupadd -r vcf_dumper_ws && useradd -r -g vcf_dumper_ws vcf_dumper_ws

# Create config directory for runtime application.properties mount
RUN mkdir -p /app/config /tmp/tomcat/logs && chown -R vcf_dumper_ws:vcf_dumper_ws /app /tmp/tomcat

# Copy the JAR file from build stage
COPY --from=build /app/vcf-dumper-ws/target/*.jar app.jar

# Set ownership
RUN chown -R vcf_dumper_ws:vcf_dumper_ws /app

USER vcf_dumper_ws

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/eva/webservices/vcf-dumper/readyz || exit 1

# JVM options for containerized environments
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
