# 🛒 Supermarket – Arquitetura em AWS com Docker, EC2 e Load Balancer

Este projeto implementa uma aplicação **Spring Boot** executando em um ambiente **AWS altamente disponível**, utilizando:

- **EC2**
- **Docker**
- **Docker Compose**
- **NGINX como Load Balancer**
- **MySQL em container**
- **Deploy automatizado via SSH**

A solução foi construída para ser **simples, escalável e fácil de manter**, ideal para portfólio profissional.

---

## 🚀 Arquitetura Geral do Projeto

A aplicação roda em **duas instâncias EC2**, cada uma executando um container Docker com o serviço Spring Boot.  
Uma terceira instância EC2 atua como **Load Balancer** utilizando NGINX.  
O banco de dados MySQL também está em uma instância EC2, rodando em container.

---

## 🏗️ Componentes da Arquitetura

### **1. Load Balancer (EC2 + NGINX)**
- Recebe todo o tráfego externo
- Distribui requisições entre as duas instâncias de aplicação
- Estratégia: **Round Robin**
- Pode ser expandido para HTTPS com Certbot

### **2. EC2-APP-JAVA-1 e EC2-APP-JAVA-2**
Cada instância contém:
- Docker Engine
- Docker Compose
- Container Spring Boot
- Porta exposta: **8080**
- Healthcheck via `/actuator/health`

### **3. Banco de Dados**
- MySQL rodando em container
- Acesso interno pela aplicação
- Isolado em instância própria

---

## 🗂️ Estrutura do Projeto

```text
supermarket/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── deploy.sh
├── docker-compose.yml
├── Dockerfile
├── nginx.conf
├── pom.xml
├── mvnw / mvnw.cmd
├── .gitignore
├── .gitattributes
└── README.md
---
```
## ☁️ Arquitetura Completa (AWS + Docker + API)

flowchart TB

    U[🧑‍💻 Usuários / Postman / Frontend] --> LB{{🔀 Load Balancer (NGINX)}}

    LB --> EC1[🖥️ EC2-APP-JAVA-1<br>Docker + Spring Boot]
    LB --> EC2[🖥️ EC2-APP-JAVA-2<br>Docker + Spring Boot]

    subgraph EC2_1[EC2-APP-JAVA-1]
        D1[🐳 Docker Engine]
        C1[📦 Docker Compose]
        API1[🚀 Spring Boot API]
    end

    subgraph EC2_2[EC2-APP-JAVA-2]
        D2[🐳 Docker Engine]
        C2[📦 Docker Compose]
        API2[🚀 Spring Boot API]
    end

    EC1 --> D1 --> C1 --> API1
    EC2 --> D2 --> C2 --> API2

    DB[(🗄️ Banco de Dados MySQL)]
    API1 --> DB
    API2 --> DB

    subgraph DEV[💻 Ambiente do Desenvolvedor]
        DEV1[🧑‍💻 Git Bash / IntelliJ]
        SH[📜 deploy.sh]
    end

    DEV1 --> SH
    SH -->|SSH + SCP| EC1
    SH -->|SSH + SCP| EC2

---

## 🏗️ Arquitetura com VPC, Subnets e Security Groups

flowchart TB

    subgraph AWS[AWS Cloud ☁️]
        
        subgraph VPC[VPC - Rede Privada]
            
            subgraph PUB[Subnet Pública 🌐]
                EC1[EC2-APP-JAVA-1<br>SG: app-sg]
                EC2[EC2-APP-JAVA-2<br>SG: app-sg]
                LB[Load Balancer (NGINX)<br>SG: lb-sg]
            end

            subgraph PRIV[Subnet Privada 🔒]
                DB[(MySQL<br>SG: db-sg)]
            end

        end
    end

    LB --> EC1
    LB --> EC2

    EC1 --> DB
    EC2 --> DB

---

## 🔄 Fluxo de Deploy

    Dev->>Local: Executa ./deploy.sh
    Local->>Local: mvn clean package
    Local->>EC1: Envia JAR + Dockerfile + docker-compose.yml (SCP)
    Local->>EC2: Envia JAR + Dockerfile + docker-compose.yml (SCP)

    Local->>EC1: docker-compose down && up -d --build
    Local->>EC2: docker-compose down && up -d --build

    EC1->>Dev: Aplicação rodando (healthy)
    EC2->>Dev: Aplicação rodando (healthy)

---

## 🧠 Fluxo Interno da API (Controller → Service → Repository)

flowchart TD

    A[Cliente / Postman] --> B[Controller]
    B --> C[Service]
    C --> D[Repository]
    D --> E[(Banco de Dados)]

    E --> D
    D --> C
    C --> B
    B --> A

---

## 🐳 Docker

### **docker-compose.yml**
O projeto utiliza Docker Compose para subir o container da aplicação:

```yaml
version: '3.8'

services:
  supermarket:
    image: supermarket:latest
    container_name: supermarket
    build: .
    ports:
      - "8080:8080"
    restart: always
```
---

## 🌐 Endpoints 

- http://44.198.61.152/swagger-ui/index.html

---
## 📈 Healthcheck

- http://44.198.61.152/actuator/health

---