# ---- Build stage ----
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy just the wrapper + pom first so dependency resolution is cached in its own layer
# — this layer only invalidates when pom.xml itself changes, not on every source edit.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Don't run as root in the container.
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

# Documents the default; Render overrides via its own PORT env var at runtime, and
# application-prod.properties reads server.port=${PORT:8080} accordingly.
EXPOSE 8080

# MaxRAMPercentage rather than a fixed -Xmx: Render's free/starter tiers are memory-
# constrained, and this keeps the heap sized relative to whatever the container
# actually gets instead of an unconditional guess.
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
