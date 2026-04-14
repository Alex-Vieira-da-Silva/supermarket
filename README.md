# Supermarket – Arquitetura AWS com Docker, EC2 e Load Balancer

API Spring Boot para gestão de supermercado (produtos, clientes e usuários), com autenticação JWT, validação, paginação e deploy automatizado em AWS. Imagens Docker armazenadas no Amazon ECR para os deploys.

---

## Principais atualizações (mar/2026)
- CRUD completo de Produtos, Clientes e Usuários com validação de DTOs.
- Filtros por nome/CPF/username e paginação padrão (`size=10`, ordenação por `id`).
- Segurança com JWT, roles ADMIN/MANAGER/USER, permissões por método e seed automático do usuário admin.
- Tratamento unificado de erros (`ApiError`) e mensagens de validação detalhadas.
- Docker Compose separado para app e banco; imagem de produção publicada no ECR.
- NGINX em instância EC2 dedicada atuando como load balancer/reverse proxy para as duas instâncias da API.
- Logs centralizados no AWS CloudWatch Logs via driver `awslogs` para app e banco.
- Pipeline GitHub Actions: build/test Maven, push para ECR e deploy via SSM em duas EC2 atrás do NGINX load balancer.

---

## Arquitetura na AWS
- VPC isolada com sub-redes para aplicação e banco.
- Duas instâncias EC2 de aplicação (APP-1 e APP-2) executando `docker-compose-app.yml`.
- Instância EC2 dedicada para MySQL (container definido em `docker-compose-db.yml`) acessível apenas pela VPC.
- Instância EC2 dedicada rodando NGINX como load balancer e reverse proxy (ver `nginx.conf`), expondo Swagger/healthcheck.
- ECR armazena a imagem `supermarket-app`; EC2s fazem `docker compose pull && up -d` durante o deploy.
- Logs são enviados para o CloudWatch Logs (grupos `supermarket-app` e `supermarket-db`) usando o driver `awslogs`; a instância precisa ter role/perfil com permissão `logs:CreateLogGroup`, `logs:CreateLogStream` e `logs:PutLogEvents`.

---

## Monitoramento e logs
- NGINX (instância EC2): access/error logs ficam no host; recomenda-se enviá-los ao CloudWatch Logs (ex.: grupo `supermarket-nginx`) via CloudWatch Agent com permissões de Logs na role da instância.
- App: `docker-compose-app.yml` define o driver `awslogs` com `awslogs-group=supermarket-app` e stream `app-${HOSTNAME}`.
- Banco: `docker-compose-db.yml` envia logs para `awslogs-group=supermarket-db` com stream `db-${HOSTNAME}`.
- Para rodar nas EC2 ou localmente com o driver `awslogs`, as credenciais/role do host devem ter permissão em CloudWatch Logs na região `us-east-1`.
- Visualização: abra o CloudWatch Logs no console AWS e filtre pelos grupos acima; cada container gera um stream por host.

---

## Tecnologias
- Java 21, Spring Boot, Spring Security, Spring Data JPA.
- JWT para autenticação stateless.
- MySQL 8.0 (H2 em memória por padrão no profile local).
- Docker e Docker Compose.
- NGINX em EC2 como load balancer/reverse proxy.
- AWS CloudWatch Logs para centralização de logs de app e banco.
- GitHub Actions + AWS SSM + ECR.

---

## Segurança e perfis
- ADMIN: CRUD de usuários, clientes e produtos.
- MANAGER: CRUD de clientes e produtos; leitura geral.
- USER: apenas endpoints de leitura (exceto recursos de usuário).
- Autenticação via JWT; endpoints públicos: `/auth/**`, `/actuator/health`, `/actuator/info`, `/swagger-ui/**`, `/v3/api-docs/**`.

---

## Fluxo de autenticação
1. Abrir `http://localhost:8080/swagger-ui/index.html`.
2. Logar em `POST /auth/login` usando as credenciais padrão `admin` / `Super@123` (ou valores de `APP_ADMIN_USERNAME` e `APP_ADMIN_PASSWORD`).
3. Enviar o token JWT no header `Authorization: Bearer <token>` nas próximas requisições.

---

## Endpoints
| Recurso | Método | Descrição | Permissão |
| :--- | :--- | :--- | :--- |
| /auth/login | POST | Gera token JWT | Público |
| /produtos | GET | Lista com paginação e filtro por `nome` | Qualquer autenticado |
| /produtos/{id} | GET | Detalhe de produto | Qualquer autenticado |
| /produtos | POST | Criar produto | ADMIN ou MANAGER |
| /produtos/{id} | PUT | Atualizar produto | ADMIN ou MANAGER |
| /produtos/{id} | DELETE | Remover produto | ADMIN ou MANAGER |
| /clientes | GET | Lista com filtros `nome` ou `cpf` + paginação | Qualquer autenticado |
| /clientes/{id} | GET | Detalhe de cliente | Qualquer autenticado |
| /clientes | POST | Criar cliente (CPF validado/formatado) | ADMIN ou MANAGER |
| /clientes/{id} | PUT | Atualizar cliente | ADMIN ou MANAGER |
| /clientes/{id} | DELETE | Remover cliente | ADMIN |
| /usuarios | GET | Lista com filtro `username` + paginação | ADMIN |
| /usuarios/{id} | GET | Detalhe de usuário | ADMIN |
| /usuarios | POST | Criar usuário (senha com força mínima) | ADMIN |
| /usuarios/{id} | PUT | Atualizar usuário | ADMIN |
| /usuarios/{id} | DELETE | Remover usuário | ADMIN |
| /actuator/health | GET | Healthcheck | Público |
| /swagger-ui/index.html | GET | Documentação | Público |

Observação: paginação usa `PageResponse` com campos `content`, `page`, `size`, `totalElements`, `totalPages`, `last`.

---

## Tratamento de erros
Erros seguem o formato `ApiError`:
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Falha na validação dos dados",
  "path": "/clientes",
  "timestamp": "2026-03-24T20:10:32.123Z",
  "details": [{"field":"cpf","message":"CPF deve estar no formato 000.000.000-00 ou conter 11 dígitos"}]
}
```
Para 401 retorna "Credenciais inválidas", para 403 "Acesso negado", 404 para recursos não encontrados e 409 para duplicidade (ex.: username ou CPF já cadastrado).

---

## Execução local
Opção 1 — H2 embutido (sem Docker)  
1. `./mvnw clean package -DskipTests`  
2. `./mvnw spring-boot:run`  
3. Swagger em `http://localhost:8080/swagger-ui/index.html`.

Opção 2 — Docker Compose com MySQL  
1. `docker compose -f docker-compose-db.yml up -d`  
2. Definir variáveis (exemplo PowerShell):  
   `set DB_URL=jdbc:mysql://localhost:3306/supermarket?useSSL=false&allowPublicKeyRetrieval=true`  
   `set DB_USER=appuser`  
   `set DB_PASSWORD=app123`  
   `set JWT_SECRET=<chave-secreta>`  
   *(se quiser enviar logs para CloudWatch em ambiente local, configure credenciais AWS com permissão em Logs e mantenha a região `us-east-1`)*  
3. `docker compose -f docker-compose-app.yml up -d`

Opção 3 — Compose para produção (imagem do ECR e healthcheck)  
1. Ajustar `ECR_IMAGE`, `DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `SWAGGER_ENABLED` em `.env`.  
2. `docker compose up -d` (usa `docker-compose.yml`).

---

## Variáveis de ambiente importantes
- DB_URL, DB_USER, DB_PASSWORD: conexão MySQL.
- JWT_SECRET e JWT_EXPIRATION: chave e expiração do token (ms).
- SWAGGER_ENABLED: habilita/oculta Swagger em produção.
- APP_ADMIN_USERNAME, APP_ADMIN_PASSWORD, APP_ADMIN_ROLE: credenciais do admin criado em bootstrap.
- SPRING_JPA_HIBERNATE_DDL_AUTO: padrão `update`.

---

## CI/CD
Workflow em `.github/workflows/deploy.yml`:
1. Maven `clean verify`.
2. Login no ECR, build e push da imagem.
3. Deploy remoto via `aws ssm send-command` para APP-1 e APP-2 executando `docker compose pull && docker compose up -d`, mantendo zero downtime com o balanceamento do NGINX.

---

## Estrutura de diretórios
```
supermarket/
|- src/
|  |- main/java/com/accenture/supermarket/
|  |  |- config/
|  |  |- controller/
|  |  |- dto/
|  |  |  |- request/
|  |  |  |- response/
|  |  |- exception/
|  |  |- mapper/
|  |  |- model/
|  |  |- repository/
|  |  |- security/
|  |  |- service/
|  |  |- util/
|  |- test/java/com/accenture/supermarket/
|  |  |- controller/
|  |  |- dto/
|  |  |- exception/
|  |  |- integration/
|  |  |- repository/
|  |  |- security/
|  |  |- service/
|  |  |- util/
|- .github/workflows/         # Pipelines GitHub Actions
|- docker-compose.yml         # Stack de produção (imagem do ECR + healthcheck)
|- docker-compose-app.yml     # Serviço da API para EC2/app hosts
|- docker-compose-db.yml      # MySQL com volume e healthcheck
|- .env                       # Variáveis de ambiente usadas no compose de produção
|- Dockerfile                 # Build da imagem (Amazon Corretto 21)
|- nginx.conf                 # Configuração do load balancer/reverse proxy
|- pom.xml                    # Dependências Maven
|- mvnw / mvnw.cmd            # Wrapper do Maven
|- target/                    # Artefatos gerados (JAR usado no Dockerfile)
|- temp.txt                   # Arquivo temporário
|- README.md                  # Este guia
```

---

## Links úteis
- Swagger em produção: http://3.224.211.157/swagger-ui/index.html#/
- Healthcheck: http://3.224.211.157/actuator/health
- Ambos os links passam pelo NGINX na instância EC2 que faz o load balancing entre APP-1 e APP-2 (ver `nginx.conf`).
