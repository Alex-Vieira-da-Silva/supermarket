# Supermarket – Arquitetura em AWS com Docker e EC2

Este projeto implementa uma aplicação **Spring Boot** executando em um ambiente **AWS altamente disponível**, utilizando **EC2**, **Docker**, **Docker Compose** e um **Load Balancer com NGINX**.

A solução foi construída para ser simples, escalável e fácil de manter.

---

## 🚀 Arquitetura do Projeto

A aplicação roda em **duas instâncias EC2**, cada uma executando um container Docker com o serviço Spring Boot.  
Um servidor adicional EC2 atua como **Load Balancer** utilizando NGINX.  
O banco de dados MySQL também está em uma instância EC2, executando dentro de um container Docker.

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
- MySQL rodando localmente na EC2
- Acesso interno pelo container

---

## 🌐 Endpoints 

- http://44.198.61.152/swagger-ui/index.html

---
## Healthcheck

- http://44.198.61.152/actuator/health

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