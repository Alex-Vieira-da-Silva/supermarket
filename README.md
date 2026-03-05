# 🛒 Supermarket – Arquitetura AWS com Docker, EC2 e Load Balancer

Este projeto implementa uma aplicação **Spring Boot** rodando em um ambiente **AWS altamente disponível**, utilizando containers Docker para garantir consistência entre os ambientes de desenvolvimento e produção. A API oferece um **CRUD completo** (Create, Read, Update, Delete) para o gerenciamento de produtos do supermercado.

---

## 🏗️ Arquitetura do Sistema

A infraestrutura foi desenhada para separar as responsabilidades de rede, aplicação e dados, garantindo alta disponibilidade e isolamento.



### **Componentes da Infraestrutura:**

* **VPC (Virtual Private Cloud):** Rede isolada que agrupa as instâncias de aplicação e o banco de dados, garantindo segurança e controle de tráfego.
* **Load Balancer (NGINX):** Atua como o ponto único de entrada. Ele recebe requisições do **Cliente** e da interface **Swagger**, distribuindo a carga entre as instâncias de aplicação.
* **Application EC2 (Nodes):** Duas instâncias rodando containers Docker com a API Spring Boot, garantindo que o sistema permaneça online mesmo em caso de falha de um nó.
* **Database EC2:** Instância dedicada rodando um container **MySQL**. Está isolada dentro da VPC, recebendo conexões apenas das instâncias de aplicação.
* **EC2 Instance (Deployer):** Máquina externa configurada para automação. Utiliza o script `deploy.sh` para enviar artefatos via **SCP** e gerenciar os containers via **SSH**.

---

## 🚀 Tecnologias Utilizadas

* **Cloud:** AWS (EC2, VPC, Security Groups, Internet Gateway).
* **Containers:** Docker & Docker Compose.
* **Backend:** Java 17 com Spring Boot.
* **Load Balancer:** NGINX (Configurado como Reverse Proxy).
* **Banco de Dados:** MySQL 8.0.
* **Documentação:** Swagger (OpenAPI).

---

## 🔧 Fluxo de Deploy Automatizado

O processo de deploy é realizado através de um script Bash automatizado:

1.  **Build Local:** O Maven gera o arquivo `.jar` da aplicação.
2.  **Transferência:** O script envia o JAR, o `Dockerfile` e o `docker-compose.yml` para as instâncias de aplicação via **SCP**.
3.  **Execução Remota:** Via **SSH**, o script comanda o Docker Compose para reconstruir as imagens e subir os containers (`down` seguido de `up --build -d`).

---

## 🔄 Zero downtime entre APP‑1 e APP‑2

* APP‑1 atualiza

* APP‑2 continua atendendo

* LB distribui tráfego

* Depois APP‑2 atualiza

* Isso é blue/green deployment na prática.

---

## 🛣️ Endpoints Principais

| Recurso                    | URL |
|:---------------------------| :--- |
| **Link da API na AWS**     | [http://3.224.211.157/swagger-ui/index.html#/](http://3.228.6.162/swagger-ui/index.html#/) |
| **Healthcheck (Actuator)** | [http://3.224.211.157/actuator/health](http://3.228.6.162/actuator/health) |

---

## 📦 Como Executar Localmente

Se desejar rodar o ambiente completo em sua máquina:

1.  **Certifique-se de ter o Docker instalado.**
2.  **Clonar o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/supermarket.git](https://github.com/seu-usuario/supermarket.git)
    cd supermarket
    ```
3.  **Subir o ambiente:**
    ```bash
    docker-compose up -d
    ```
4.  **Acessar:** `http://localhost:8080/swagger-ui/index.html`

---

## 🧠 Estrutura de Diretórios

```text
supermarket/
├── src/                        # Código-fonte Java (Spring Boot)
├── target/                     # Binários gerados após o build (Maven)
├── deploy.sh                   # Script de automação de deploy (SCP/SSH)
├── docker-compose.yml          # Orquestração principal (Geral)
├── docker-compose-app.yml      # Configuração específica do container da API
├── docker-compose-db.yml       # Configuração específica do banco de dados
├── Dockerfile                  # Receita para build da imagem da aplicação
├── nginx.conf                  # Configuração do Load Balancer
├── pom.xml                     # Gerenciador de dependências Maven
├── mvnw / mvnw.cmd             # Wrapper do Maven
└── README.md                   # Documentação do projeto
