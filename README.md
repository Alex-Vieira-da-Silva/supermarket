# Supermarket – Arquitetura AWS com Docker, EC2 e Load Balancer

Aplicação Spring Boot rodando em um ambiente AWS de alta disponibilidade, containerizada com Docker. A API expõe um CRUD completo para gerenciamento de produtos do supermercado.

---

## Arquitetura do Sistema

- VPC: rede isolada que agrupa instâncias de aplicação e banco de dados, controlando o tráfego.
- Load Balancer (NGINX): ponto único de entrada; recebe requisições de clientes/Swagger e distribui para as instâncias de aplicação.
- Application EC2 (APP-1 e APP-2): cada nó roda o stack via `docker-compose-app.yml`, puxando a imagem da aplicação diretamente do ECR.
- Database EC2: instância dedicada rodando container MySQL (definido em `docker-compose-db.yml`), acessível apenas pela VPC.
- ECR: registro de imagens Docker usado pelo pipeline de deploy (build/push no CI; pull nos nós via `docker compose pull && up -d`).

---

## Tecnologias Utilizadas

- Cloud: AWS (EC2, VPC, Security Groups, Internet Gateway, ECR, SSM).
- Containers: Docker e Docker Compose.
- Backend: Java 17 com Spring Boot.
- Load Balancer: NGINX (reverse proxy).
- Banco de Dados: MySQL 8.0.
- Documentação: Swagger (OpenAPI).

---

## Fluxo de CI/CD (GitHub Actions + ECR + SSM)

O deploy agora é automatizado via GitHub Actions em `.github/workflows/deploy.yml`:

1. Build e testes: `mvn -B clean verify` (testes não são ignorados).
2. Login no Amazon ECR e build/push da imagem: imagem enviada para `${{ secrets.ECR_REPOSITORY }}` no registry retornado pelo login.
3. Deploy remoto: via `aws ssm send-command` para `${{ secrets.EC2_APP_1 }}` e `${{ secrets.EC2_APP_2 }}`, executando `docker compose pull && docker compose up -d` nas instâncias para atualizar os containers.

Pré-requisitos de secrets no GitHub:
- `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`
- `ECR_REPOSITORY`
- `EC2_APP_1`, `EC2_APP_2`

---

## Zero downtime entre APP-1 e APP-2

- Atualiza APP-1 enquanto APP-2 atende.
- Load Balancer distribui o tráfego.
- Atualiza APP-2 e normaliza o balanço.
- Resultado: implantação estilo blue/green.

---

## Autenticação

1. Acesse: `http://localhost:8080/swagger-ui/index.html`
2. Login (passo a passo):
   1. Use as credenciais padrão para login:  
      - **username:** `admin`  
      - **password:** `Admin@123`  
   2. Envie `POST /auth/login` com o corpo `{"username":"admin","password":"Admin@123"}` (ou as credenciais que definiu).
   3. Copie o token JWT da resposta e envie nas próximas requisições no header `Authorization: Bearer <token>`.

## Permissões por Role

- **ADMIN:** ler, criar, editar e deletar usuários, produtos e clientes.
- **MANAGER:** ler tudo; criar, editar e deletar produtos; criar e editar clientes.
- **USER:** pode ler todos os GETs, exceto os da tabela Usuário; não pode criar, ler, atualizar nem excluir registros de Usuário.

---

## Endpoints Principais

| Recurso | URL |
| :--- | :--- |
| Link da API na AWS | http://3.224.211.157/swagger-ui/index.html#/ |
| Healthcheck (Actuator) | http://3.224.211.157/actuator/health |
| Autenticação | POST /auth/login (body: username, password) |

---

## Como Executar Locamente

1. Instale Docker.
2. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/supermarket.git
   cd supermarket
   ```
3. Suba o ambiente:
   ```bash
   docker-compose up -d
   ```

---

## Estrutura de Diretórios

```text
supermarket/
├── src/                       # Código-fonte Java (Spring Boot)
├── target/                    # Binários gerados após o build (Maven)
├── .github/workflows/         # Pipelines GitHub Actions (deploy para AWS/ECR/SSM)
├── deploy.sh                  # Script legado de deploy via SCP/SSH (não usado no pipeline atual)
├── docker-compose.yml         # Orquestração principal
├── docker-compose-app.yml     # Configuração do container da API
├── docker-compose-db.yml      # Configuração do banco de dados
├── Dockerfile                 # Receita para build da imagem da aplicação
├── nginx.conf                 # Configuração do load balancer
├── pom.xml                    # Gerenciador de dependências Maven
├── mvnw / mvnw.cmd            # Wrapper do Maven
└── README.md                  # Documentação do projeto
