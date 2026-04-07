# Financy

![Página Dashboard](screenshots/dashboard_page.png)
![Página Transações](screenshots/transaction_page.png)
![Página Categorias](screenshots/categories_page.png)

## Sobre o Projeto

Aplicação full-stack para controle de finanças pessoais, permitindo gerenciar transações e categorias com autenticação segura via JWT. Construído com o intuito de aprender a desenvolver aplicações front-end em angular e autenticação de usuários com o Spring Security, além de integração entre frontend e backend.

Design do site seguido conforme o protótipo do Figma da rocketseat: https://www.figma.com/community/file/1580994817007013257/financy

## Funcionalidades

- Criação de conta
- Autenticação
  - Login com geração de token JWT
  - Rotas protegidas com Spring Security
  - Validação de token em cada requisição
- CRUD de categorias
- CRUD de transações
- Alteração de nome do usuário

## Tecnologias Utilizadas

- Java 21
  - Spring Boot 4.0.3
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
  - Lombok
- Angular 21
- PostgreSQL 17
- Docker

## Visualizando o Projeto

O projeto está acessível online em: https://financy-angular-java.vercel.app/

E o Swagger em: https://financy-angular-java.onrender.com/swagger-ui/index.html

**Obs:** A API pode demorar cerca 3 minutos para "acordar", pois como está hospedada no render de forma gratuita, acaba sendo desligada depois de um tempo sem requisições.

## Executando o Projeto Localmente com Docker

O projeto utiliza Docker Compose para subir:

- Frontend (Angular)
- Backend (Spring Boot)
- Banco de dados (PostgreSQL)

### Pré-requisitos
- Docker

1. Clone o repositório:
```bash
git clone https://github.com/zNathan2303/financy-angular-java
```

2. Na raiz do projeto, rode:
```bash
docker compose up --build
```

3. Acesse http://localhost:4200, crie uma conta, faça login, e teste as funcionalidades!

## Executando o Projeto Localmente (Sem Docker)

### Pré-requisitos
- JDK +21
- Node.js +20.19.0
- npm
- Angular CLI +21
- PostgreSQL 17

Clone o repositório:
```bash
git clone https://github.com/zNathan2303/financy-angular-java
```

### Banco de Dados

1. Crie um banco de dados no PostgreSQL.

2. Crie as tabelas do script que estão em `database/tables.sql`

### Backend

1. Abra o projeto em `backend`.

2. Em `src/main/resources/application.properties`, Altere os campos:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_database
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
jwt.secret=sua_chave_secreta <-- Chave para assinar e validar os tokens JWT.
```

3. Inicie a aplicação.

### Frontend

1. Abra o projeto em `frontend`.

2. Baixe as dependências:
```bash
npm install
```

3. Rode a aplicação no modo de desenvolvimento:
```bash
ng serve --configuration development
```

4. Acesse http://localhost:4200, crie uma conta, faça login, e teste as funcionalidades!

## Autor

[Nathan da Silva Costa](https://www.linkedin.com/in/nathandasilvacosta/)
