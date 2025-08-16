# 📦 Favorite Products API

## Descrição
A **Favorite Products API** é uma aplicação RESTful desenvolvida em **Java 17** com **Spring Boot**.  
Ela permite gerenciar clientes e seus produtos favoritos, incluindo operações como adicionar, listar e remover favoritos, além de gerenciar informações de clientes.  
A API segue boas práticas como **Clean Code**, **SOLID** e **Clean Architecture**.

---

## Objetivo
Fornecer uma solução eficiente para gerenciar *clientes* e seus *produtos favoritos*, com autenticação via **JWT** e integração com a [FakeStore API](https://fakestoreapi.com/).

---

## 🛠 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Security (JWT)**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Swagger / OpenAPI**
- **Arquitetura Hexagonal**

---

## 📁 Estrutura do Projeto

```plaintext
src
├── adapter
│   ├── in       # Controllers
│   └── out      # Repositórios
├── application
│   └── service  # Regras de negócio
├── config       # Segurança, Swagger, etc.
├── domain
│   ├── model    # Entidades
│   └── port     # Interfaces (Ports)
└── exception    # Manipulação de erros
└── util         # Utilitários
```

## ⚙ Como Rodar
- Clonar o projeto:

```bash
git clone https://github.com/seu-usuario/favorite-products-api.git
```
- Navegar até o diretório do projeto:
```bash
cd favorite-products-api
```
- Certifique-se de ter o **Java 17** e o **PostgreSQL** instalados.

## Criar o arquivo .env:

cp .env.example .env
Edite o arquivo .env com suas variáveis reais.

## Executar a aplicação:
./mvnw clean install
./mvnw spring-boot:run

chmod +x start.sh
./start.sh
<hr>    </hr>

## 🔐 Autenticação
- Usuário padrão:
```plaintext
username: admin
password: admin123
```
- A autenticação é feita via **JWT**.
- Login:
- POST /auth/login
- Para autenticar, utilize o seguinte exemplo de requisição:
```json
{
  "username": "admin",
  "password": "admin123"
}
```
- Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

- Use o token retornado no header `Authorization` para acessar endpoints protegidos:
```json
Authorization: Bearer <token>
```


