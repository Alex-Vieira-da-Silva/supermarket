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








