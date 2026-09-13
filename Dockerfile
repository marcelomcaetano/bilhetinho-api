FROM maven:3.9.6-eclipse-temurin-21-alpine

WORKDIR /app

# Copia o código-fonte e arquivos de configuração da API
COPY . .

# Compila a aplicação e gera o executável JAR
RUN mvn clean package -DskipTests

# Exposição da porta da API
EXPOSE 8080

# Executa o arquivo JAR gerado
CMD ["java", "-jar", "target/bilhetinho-api.jar"]
