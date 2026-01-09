# 🛒 Supermarket API

A **Supermarket API** é uma aplicação REST desenvolvida para gerenciar produtos de supermercado de forma simples, rápida e escalável.  
Construída com **Spring Boot**, integrada a um banco **MySQL**, conteinerizada com **Docker** e preparada para ambiente de produção com deploy em **AWS EC2**, ela oferece uma base sólida para estudos, testes ou evolução para um sistema real.

Esta API permite realizar operações completas de CRUD (Create, Read, Update, Delete) de produtos, além de fornecer endpoints de monitoramento via **Spring Actuator**, garantindo observabilidade e saúde da aplicação em produção.

---

## 🚀 Objetivos do Projeto

- Demonstrar uma arquitetura moderna utilizando **Java + Spring Boot**  
- Aplicar boas práticas de desenvolvimento backend  
- Utilizar **Docker** para padronizar ambiente e facilitar deploy  
- Integrar com banco de dados relacional **MySQL**  
- Realizar deploy em ambiente real na **AWS EC2**  
- Servir como base para estudos, testes e evolução para sistemas maiores  

---

## 🧩 Principais Recursos

- API REST completa para gerenciamento de produtos  
- Persistência com Spring Data JPA  
- Banco de dados MySQL rodando em container Docker  
- Healthcheck com Spring Actuator  
- Deploy automatizado via Docker Compose  
- Estrutura limpa e organizada seguindo boas práticas  
- Fácil escalabilidade e manutenção  

---

## 🏗️ Arquitetura da Solução

A aplicação segue uma arquitetura simples e eficiente:

- **Controller** → recebe requisições HTTP  
- **Service** → contém regras de negócio  
- **Repository** → comunicação com o banco via JPA  
- **Model** → entidades persistidas no MySQL  

O ambiente é orquestrado com **Docker Compose**, contendo:

- Container da aplicação Spring Boot  
- Container do MySQL  
- Healthcheck automático  
- Restart automático em caso de falhas  

---

## 🌐 Tecnologias Utilizadas

- **Java 17**  
- **Spring Boot 3+**  
- **Spring Web**  
- **Spring Data JPA**  
- **Spring Actuator**  
- **MySQL 8**  
- **Docker & Docker Compose**  
- **AWS EC2 (Amazon Linux 2023)**  

---

# 🛒 Supermarket API

![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge)
![AWS EC2](https://img.shields.io/badge/AWS-EC2-FF9900?style=for-the-badge)
![License](https://img.shields.io/badge/License-Free-blue?style=for-the-badge)

A **Supermarket API** é uma aplicação REST desenvolvida para gerenciar produtos de supermercado de forma simples, rápida e escalável.  
Construída com **Spring Boot**, integrada a um banco **MySQL**, conteinerizada com **Docker** e preparada para ambiente de produção com deploy em **AWS EC2**.

---

## 🧩 Principais Recursos

- CRUD completo de produtos  
- Persistência com Spring Data JPA  
- Banco MySQL rodando em container Docker  
- Healthcheck com Spring Actuator  
- Deploy automatizado via Docker Compose  
- Arquitetura limpa e escalável  

---

## 📂 Estrutura do Projeto

supermarket/
 ├── src/
 │   ├── main/
 │   │   ├── java/com/accenture/supermarket/
 │   │   │   ├── controller/
 │   │   │   ├── service/
 │   │   │   ├── repository/
 │   │   │   ├── model/
 │   │   │   └── SupermarketApplication.java
 │   │   └── resources/
 │   │       ├── application.properties
 │   │       └── static/
 ├── Dockerfile
 ├── docker-compose.yml
 ├── pom.xml
 └── README.md

 ---

## 🌐 Endpoints principais

➤ Criar produto

POST /produtos

Body:
{
  "nome": "Arroz Tipo 1",
  "preco": 22.90,
  "quantidade": 50
}


➤ Listar produtos

[
  {
    "id": 1,
    "nome": "Arroz Tipo 1",
    "preco": 22.90,
    "quantidade": 50
  },
  {
    "id": 2,
    "nome": "Feijão Carioca",
    "preco": 7.49,
    "quantidade": 80
  }
]


➤ Buscar por ID

GET /produtos/{id}


➤ Atualizar produto

PUT /produtos/{id}


➤ Deletar produto

DELETE /produtos/{id}


🩺 Healthcheck

GET /actuator/health








