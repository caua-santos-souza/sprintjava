# Use a imagem oficial do OpenJDK 17
FROM openjdk:17-jdk-slim

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copiar código fonte
COPY src src

# Dar permissão de execução para o wrapper do Maven
RUN chmod +x mvnw

# Construir a aplicação
RUN ./mvnw clean package -DskipTests

# H2 em memória não precisa de diretório persistente

# Expor porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-Dspring.profiles.active=render", "-jar", "target/trackyard-0.0.1-SNAPSHOT.jar"]
