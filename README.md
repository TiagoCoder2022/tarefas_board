# 📋 Board Tarefas

Sistema de gerenciamento de tarefas estilo Kanban via linha de comando (CLI), desenvolvido com Java, Spring Boot e MySQL. Permite criar boards personalizados, gerenciar cards em colunas, e controlar o fluxo de trabalho com funcionalidades de bloqueio e movimentação.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)

## 🚀 Funcionalidades

- **Gestão de Boards**: Crie boards personalizados com colunas do tipo:
  - 🟢 **Initial** (Inicial) - obrigatória
  - 🟡 **Pending** (Pendente) - opcionais customizáveis
  - 🔵 **Final** (Final) - obrigatória
  - 🔴 **Canceled** (Cancelamento) - obrigatória

- **Gestão de Cards**:
  - Criar cards com título e descrição
  - Mover cards entre colunas
  - Cancelar cards (move para coluna de cancelamento)
  - Visualizar detalhes do card

- **Bloqueio de Cards**:
  - Bloquear cards com motivo
  - Desbloquear cards com motivo
  - Histórico de bloqueios
  - Prevenção de bloqueio em colunas finais/canceladas

- **Visualização**:
  - Visualizar board completo com contagem de cards
  - Visualizar colunas específicas com seus cards
  - Visualizar detalhes individuais de cards

## 🛠️ Tecnologias

- **Java 21**
- **Spring Boot 3.x** (JDBC, Validation)
- **MySQL 8.0**
- **Flyway** (Migrations)
- **Maven**

## 📋 Pré-requisitos

- Java 21 ou superior
- MySQL 8.0+
- Maven 3.8+

## 🔧 Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/board-tarefas.git
cd board-tarefas
```
### 2. Configure o banco de dados
Crie o banco de dados no MySQL:
```bash
CREATE DATABASE board_tarefas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
Configure as credenciais em src/main/resources/application.properties:

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/board_tarefas
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

### 3. Execute as migrations
```bash
mvn flyway:migrate
```
Ou execute a aplicação que as migrations serão aplicadas automaticamente.

### 4. Execute a aplicação
```bash 
mvn spring-boot:run 
```
## 🎮 Como Usar
Ao iniciar, você verá o menu principal:
Bem vindo ao gerenciamento de boards, escolha a opcao desejada
1 - Criar um novo board
2 - Selecionar um board
3 - Excluir um board
4 - Sair

### Criando um Board
   1 - Escolha a opção 1
   2 - Informe o nome do board
   3 - Defina quantas colunas pendentes adicionais deseja (além das 3 padrões)
   4 - Informe os nomes das colunas conforme solicitado

### Gerenciando um Board
Após selecionar um board (opção 2), você terá acesso ao menu de operações:

Bem vindo ao board [ID], selecione a operacao desejada
1 - Criar um card
2 - Mover um card
3 - Bloquear um card
4 - Desbloquear um card
5 - Cancelar um card
6 - Visualizar board
7 - Visualizar colunas com cards
8 - Ver cards
9 - Voltar para o menu anterior
10 - Sair

## Fluxo de Trabalho Típico

  1 - Criar Card (Opção 1) → Adiciona na coluna inicial
  2 - Mover Card (Opção 2) → Avança para próxima coluna
  3 - Bloquear Card (Opção 3) → Se encontrar impedimento
  4 - Desbloquear Card (Opção 4) → Quando resolver o impedimento
  5 - Cancelar Card (Opção 5) → Se necessário descartar

  ## 📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.
