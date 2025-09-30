# Dockerfile multistage para aplicação Spring Boot com Oracle

# Etapa 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Cria usuário não-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copia jar do build
COPY --from=build /app/target/trackyard-0.0.1-SNAPSHOT.jar app.jar

# Porta exposta
EXPOSE 8080

# Variáveis de ambiente para Oracle (valores default)
ENV DB_USER=rm559093 \
    DB_PASSWORD=fiap \
    ORACLE_HOST=oracle.fiap.com.br \
    ORACLE_PORT=1521 \
    ORACLE_SID=orcl

# Executa como não-root
USER appuser

# Entrypoint com profile Azure
ENTRYPOINT ["java", "-Dspring.profiles.active=azure", "-jar", "app.jar"]
