# 🛒 Supermarket – Arquitetura AWS com Docker, EC2 e Load Balancer

Este projeto implementa uma aplicação **Spring Boot** rodando em um ambiente **AWS altamente disponível**, utilizando containers Docker para garantir consistência entre os ambientes de desenvolvimento e produção.

---

## 🏗️ Arquitetura do Sistema

Abaixo, o diagrama técnico que detalha a infraestrutura na nuvem conforme implementado:

![Arquitetura da Aplicação](./img/Diagrama%20da%20aplicação.jpg)

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

## 🛣️ Endpoints Principais

| Recurso | URL |
| :--- | :--- |
| **Documentação API (Swagger)** | [http://3.228.6.162/swagger-ui/index.html#/](http://3.228.6.162/swagger-ui/index.html#/) |
| **Healthcheck (Actuator)** | [http://3.228.6.162/actuator/health](http://3.228.6.162/actuator/health) |

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
├── img/                # Diagramas e prints do projeto
├── src/                # Código fonte (Spring Boot)
├── deploy.sh           # Automação de deploy (SCP/SSH)
├── docker-compose.yml  # Orquestração de containers
├── Dockerfile          # Definição da imagem da aplicação
├── nginx.conf          # Configuração do Load Balancer
└── pom.xml             # Gerenciador de dependências Maven