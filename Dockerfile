# Use a imagem oficial do OpenJDK 17 com Maven
FROM openjdk:17-jdk-slim

# Instalar Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY trackyard/pom.xml .

# Copiar código fonte
COPY trackyard/src src

# Construir a aplicação usando Maven
RUN mvn clean package -DskipTests

# H2 em memória não precisa de diretório persistente

# Expor porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-Dspring.profiles.active=render", "-jar", "target/trackyard-0.0.1-SNAPSHOT.jar"]
