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

## Endpoints Principais

| Recurso | URL |
| :--- | :--- |
| Link da API na AWS | http://3.224.211.157/swagger-ui/index.html#/ |
| Healthcheck (Actuator) | http://3.224.211.157/actuator/health |
| Autenticação | POST /auth/login (body: username, password) |

---

## Como Executar Localmente

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
4. Acesse: `http://localhost:8080/swagger-ui/index.html`
5. Autentique: por padr\u00e3o ser\u00e1 criado um usu\u00e1rio admin (`username: admin`, `password: Admin@123`). Estes valores podem ser alterados via vari\u00e1veis `APP_ADMIN_USERNAME`, `APP_ADMIN_PASSWORD` e `APP_ADMIN_ROLE`. Chame `POST /auth/login` com o JSON `{"username":"admin","password":"Admin@123"}` (ou as credenciais que voc\u00ea definir) e use o token retornado no header `Authorization: Bearer <token>` nas demais chamadas.

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

---

## Segurança (resumo)

- Autenticação via JWT: `/auth/login` gera o token; demais rotas exigem `Authorization: Bearer <token>`.
- Roles: produtos e usuários só podem ser criados/alterados/deletados por `ROLE_ADMIN`; clientes por `ROLE_ADMIN` ou `ROLE_MANAGER` (delete apenas `ADMIN`).
- Senhas armazenadas com BCrypt; políticas de senha aplicadas no DTO (mín. 8 chars, maiúscula/minúscula/dígito/especial).
- Variáveis sensíveis via ambiente: `DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION`, `SWAGGER_ENABLED`.
```

---

## PermissÃµes por Role

- `ADMIN`: CRUD completo de usuÃ¡rios, produtos e clientes.
- `MANAGER`: criar/atualizar/deletar produtos; criar/atualizar clientes; leitura geral (todos os GETs).
- `USER`: apenas leitura (GETs) dos recursos expostos.

## Notas

- O pipeline oficial é o GitHub Actions; `deploy.sh` permanece apenas como referência/legado.
- As instâncias EC2 precisam ter Docker e Docker Compose instalados e configurados para o comando `docker compose pull && docker compose up -d`.
