# Anhembi Project Manager - Backend

## Sobre o Projeto

Este é o backend da aplicação Anhembi Project Manager, um sistema de gerenciamento de projetos projetado para equipes. Ele permite o cadastro de usuários, criação de equipes, gerenciamento de projetos e atribuição de tarefas.

## Tecnologias Utilizadas

- **Java 21**: Plataforma de desenvolvimento.
- **Spring Boot**: Framework para criação de aplicações Java.
- **Spring Security**: Para autenticação e autorização baseada em JWT.
- **JPA (Hibernate)**: Para persistência de dados.
- **Maven**: Para gerenciamento de dependências e build da aplicação.

## Como Executar a Aplicação

### Pré-requisitos

- Java 21 ou superior instalado.
- Maven instalado.

### Passos

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-repositorio>
    ```
2.  **Navegue até o diretório do backend:**
    ```bash
    cd anhembi-project-manager/backend
    ```
3.  **Execute a aplicação com o Maven:**
    ```bash
    ./mvnw spring-boot:run
    ```
    No Windows, use `mvnw.cmd spring-boot:run`.

A aplicação estará disponível em `http://localhost:8080`.
