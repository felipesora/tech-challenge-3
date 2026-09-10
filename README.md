# Tech Challenge – Fase 3

Backend modular para gerenciamento de consultas em ambiente hospitalar, desenvolvido para o Tech Challenge da Fase 3 da Pós Tech – Arquitetura e Desenvolvimento Java.

A solução foi dividida em três serviços e utiliza Spring Security com JWT, GraphQL para consulta do histórico do paciente e RabbitMQ para comunicação assíncrona entre o serviço de agendamento e o serviço de notificações.

## Arquitetura

```text
                       +----------------------+
                       |     user-service     |
                       |       :8080          |
                       | usuários / login JWT |
                       +----------+-----------+
                                  |
                                  | JWT compartilhado
                                  v
+----------------------+   RabbitMQ   +----------------------+
| appointment-service  | -----------> | notification-service |
|       :8081          | notification |       :8082          |
| REST + GraphQL       |    .queue    | notificações in-app  |
+----------+-----------+              +----------+-----------+
           |                                     |
           v                                     v
     PostgreSQL                              PostgreSQL
     appointments                            notifications

user-service também utiliza seu próprio banco PostgreSQL `users`.
```

### Serviços

| Serviço | Porta | Responsabilidade |
|---|---:|---|
| `user-service` | 8080 | Cadastro de usuários, tipos de usuário, login e geração de JWT |
| `appointment-service` | 8081 | Criação, edição e consulta de consultas; GraphQL; publicação de eventos no RabbitMQ |
| `notification-service` | 8082 | Consumo dos eventos do RabbitMQ e disponibilização das notificações aos pacientes |
| PostgreSQL | 5433 | Persistência dos três serviços em bancos separados |
| RabbitMQ | 5672 | Comunicação assíncrona |
| RabbitMQ Management | 15672 | Interface administrativa do RabbitMQ |

## Tecnologias

- Java 21
- Spring Boot 4.0.7
- Spring Security
- JWT (`jjwt`)
- Spring Data JPA / Hibernate
- PostgreSQL 16
- Spring for GraphQL
- RabbitMQ / Spring AMQP
- Springdoc OpenAPI / Swagger
- JUnit 5 / Mockito
- JaCoCo
- Docker / Docker Compose

## Perfis e permissões

O JWT carrega a identificação e o perfil do usuário autenticado.

| Funcionalidade | Médico | Enfermeiro | Paciente |
|---|:---:|:---:|:---:|
| Criar consulta | ✅ | ✅ | ❌ |
| Editar consulta | ✅ | ✅ | ❌ |
| Consultar histórico de paciente via GraphQL | ✅ | ✅ | somente o próprio |
| Consultar consultas futuras via GraphQL | ✅ | ✅ | somente as próprias |
| Listar todas as consultas via REST | ❌ | ✅ | ❌ |
| Consultar notificações de paciente | ❌ | ✅ | somente as próprias |
| Listar todas as notificações | ❌ | ✅ | ❌ |

Além do controle por `@PreAuthorize`, as consultas GraphQL validam o ID do paciente autenticado. Dessa forma, um usuário com perfil `PACIENTE` não consegue informar o UUID de outro paciente para acessar seu histórico.

## Comunicação assíncrona

Ao criar uma consulta, o `appointment-service` publica um evento do tipo `LEMBRETE` na fila:

```text
notification.queue
```

Ao editar uma consulta, é publicado um evento do tipo `ALTERACAO`.

O `notification-service` consome a mensagem por meio de `@RabbitListener`, cria a notificação com status `PENDENTE`, disponibiliza a notificação pelo canal in-app e, após o processamento, atualiza o status para `ENVIADA` e preenche `enviadoEm`.

O envio foi abstraído pela interface `NotificacaoSender`. A implementação atual (`InAppNotificacaoSender`) representa o canal in-app e registra o despacho no log. A abstração permite substituir esse adaptador posteriormente por e-mail, SMS ou push sem alterar o caso de uso.

Caso ocorra erro no adaptador de envio, a notificação é persistida com status `ERRO`.

## Regra de conflito de agenda

Uma consulta não pode ser criada se o médico já tiver outra consulta no mesmo horário.

A mesma validação é realizada na edição. Na alteração é utilizada uma consulta que exclui o próprio ID da consulta sendo editada, evitando falso conflito quando apenas outros dados são modificados.

## Endpoints principais

### user-service – `http://localhost:8080`

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/auth/register` | Cadastra usuário |
| POST | `/auth/login` | Autentica e retorna JWT |
| GET | `/tipos-usuario` | Lista tipos de usuário |
| POST | `/tipos-usuario` | Cadastra tipo de usuário |
| GET | `/tipos-usuario/{id}` | Busca tipo por ID |
| GET | `/usuarios` | Lista usuários – médico/enfermeiro |
| GET | `/usuarios/{id}` | Busca usuário conforme regras de acesso |

### appointment-service – `http://localhost:8081`

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/consultas` | Cria uma consulta – médico/enfermeiro |
| PUT | `/consultas/{id}` | Edita uma consulta – médico/enfermeiro |
| GET | `/consultas` | Lista todas as consultas – enfermeiro |
| POST | `/graphql` | Endpoint GraphQL |

### notification-service – `http://localhost:8082`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/notificacoes` | Lista todas as notificações – enfermeiro |
| GET | `/notificacoes/paciente/{id}` | Notificações de um paciente |

## GraphQL

Endpoint:

```text
POST http://localhost:8081/graphql
```

### Histórico de um paciente

```graphql
query {
  historicoPaciente(pacienteId: "UUID_DO_PACIENTE") {
    id
    pacienteId
    medicoId
    enfermeiroId
    dataHora
    status
    observacoes
  }
}
```

### Consultas futuras

```graphql
query {
  consultasFuturasPaciente(pacienteId: "UUID_DO_PACIENTE") {
    id
    dataHora
    status
    observacoes
  }
}
```

Para um usuário `PACIENTE`, o `pacienteId` deve ser o mesmo ID contido no token JWT. Médico e enfermeiro podem consultar pacientes distintos conforme suas permissões.

## Autenticação

Faça o login no `user-service`:

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "email": "usuario@email.com",
  "senha": "senhaforte"
}
```

Utilize o token retornado nas chamadas protegidas:

```http
Authorization: Bearer SEU_TOKEN_JWT
```

Os três serviços utilizam a mesma chave JWT quando executados pelo `docker-compose.yml`, permitindo que `appointment-service` e `notification-service` validem o token emitido pelo `user-service`.

## Executando com Docker Compose

Pré-requisitos:

- Docker
- Docker Compose

Na raiz do projeto:

```bash
docker compose up --build
```

Para acompanhar os serviços:

```bash
docker compose ps
```

Para encerrar:

```bash
docker compose down
```

Caso seja necessário recriar também os bancos do zero:

```bash
docker compose down -v
docker compose up --build
```

> A opção `-v` remove o volume local do PostgreSQL e, portanto, apaga os dados do ambiente de desenvolvimento.

## Swagger / OpenAPI

Com os serviços em execução:

- user-service: `http://localhost:8080/swagger-ui/index.html`
- appointment-service: `http://localhost:8081/swagger-ui/index.html`
- notification-service: `http://localhost:8082/swagger-ui/index.html`

## RabbitMQ Management

Interface administrativa:

```text
http://localhost:15672
```

A fila utilizada pela aplicação é `notification.queue`.

## Collection do Postman

O projeto contém a collection:

```text
Tech Challenge 3 - Fluxo Completo Automatizado v2.postman_collection.json
```

Fluxo principal da collection:

1. consulta os tipos de usuário criados pelo seeder;
2. cadastra paciente, enfermeiro e médico;
3. realiza login;
4. agenda consulta;
5. valida a notificação;
6. edita a consulta;
7. valida a notificação de alteração;
8. consulta o histórico via GraphQL;
9. realiza login como paciente;
10. valida que o paciente acessa o próprio histórico;
11. valida que o paciente não consegue acessar o histórico de outro paciente.

## Testes

Cada serviço possui testes unitários em `src/test/java`.

Para executar os testes localmente, dentro de cada serviço:

```bash
./mvnw test
```

No Windows:

```bat
mvnw.cmd test
```

Para gerar o relatório do JaCoCo:

```bash
./mvnw verify
```

O relatório será criado em:

```text
target/site/jacoco/index.html
```

## Estrutura simplificada

```text
tech-challenge-3/
├── user-service/
├── appointment-service/
├── notification-service/
├── db-config/
│   └── init.sql
├── docker-compose.yml
├── Tech Challenge 3 - Fluxo Completo Automatizado v2.postman_collection.json
└── README.md
```

## Observação sobre autenticação

O projeto utiliza Spring Security com JWT Bearer. O objetivo é fornecer autenticação stateless adequada à arquitetura dividida em serviços, mantendo os níveis de acesso de médico, enfermeiro e paciente.
