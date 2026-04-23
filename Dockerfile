# Stage 1 — Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline --batch-mode -q

COPY src ./src
RUN mvn package -DskipTests --batch-mode -q

RUN java -Djarmode=tools -jar target/*.jar extract --layers --launcher --destination target/extracted

# Stage 2 — Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /build/target/extracted/dependencies ./
COPY --from=builder /build/target/extracted/spring-boot-loader ./
COPY --from=builder /build/target/extracted/snapshot-dependencies ./
COPY --from=builder /build/target/extracted/application ./

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
