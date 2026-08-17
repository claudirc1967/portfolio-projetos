# Portfólio de Projetos

Aplicação de cadastro e acompanhamento de projetos, desenvolvida em **Java 6** com **Jaguar/jCompany 6.1.5** (JSF + Hibernate), PostgreSQL e Tomcat 6 da suíte. Não utiliza Spring Boot.

## Stack

| Item | Versão / detalhe |
|---|---|
| JDK | 6 (`JAVA_HOME` apontando para o JDK 6 da suíte) |
| Framework | Jaguar / jCompany 6.1.5 |
| Persistência | Hibernate JPA + PostgreSQL (`PostgreSQLDialect`) |
| Servidor | Tomcat 6 da suíte Jaguar |
| REST | JAX-RS (RESTEasy), prefixo `/soa` |
| Testes | JUnit 3 (`junit.framework.TestCase`) |
| Segurança | FORM login do container (`admin` / `senha`) |

Módulos Maven: `_commons` (entidades/DTOs), `_model` (DAO/Manager), `portfolio-projetos` (WAR).

## Pré-requisitos

1. PostgreSQL local com o banco `portfolio_projetos`.
2. Usuário `portfolio` / senha `portfolio` (JNDI em `portfolio-projetos/src/main/portfolio-projetos.xml`).
3. Datasource: `jdbc/portfolio-projetos`.

## Como executar

1. Confirme `JAVA_HOME` no JDK 6 da suíte.
2. No Eclipse, no projeto parent, rode o External Tool **Liberacao Para Tomcat Completa Desenvolvimento** (Maven `clean install` da suíte, preferencialmente `-o`).
3. Acesse: http://localhost:8080/portfolio-projetos
4. Login: **admin** / **senha**.

Tela principal: **Projeto** (`/f/n/projeto`). Membros **não** têm CRUD JSF; entram só pela API REST mockada.

## Regras de negócio

- **Risco** é calculado (não persistido): baixo (orçamento ≤ 100.000 e prazo ≤ 3 meses); médio (100.001–500.000 ou 3–6 meses); alto (> 500.000 ou > 6 meses).
- **Status** segue a sequência: em análise → análise realizada → análise aprovada → iniciado → planejado → em andamento → encerrado. Cancelado pode ocorrer a qualquer momento, **exceto** a partir de encerrado. Não é permitido pular etapa.
- **Exclusão** bloqueada se o status for iniciado, em andamento ou encerrado.
- Somente membro com atribuição **funcionário** pode ser gerente/alocado.
- Entre 1 e 10 membros por projeto; no máximo 3 projetos ativos simultâneos por membro.

## API REST

Base: `http://localhost:8080/portfolio-projetos/soa`

`/soa/*` está protegido por FORM. No Postman:

1. `GET` em qualquer URL protegida (ex.: `/soa/membros`) → recebe a tela de login.
2. `POST` `http://localhost:8080/portfolio-projetos/j_security_check`  
   body `x-www-form-urlencoded`: `j_username=admin` e `j_password=senha`.
3. Reutilize o cookie `JSESSIONID` nas próximas chamadas.

### Membros (CRUD mockado)

**POST** `/soa/membros`  
`Content-Type: application/json`

```json
{
  "nome": "Ana Silva",
  "atribuicao": "funcionário"
}
```

Resposta **201**: `{ "id": 1, "nome": "Ana Silva", "atribuicao": "funcionário" }`

**GET** `/soa/membros`  
Lista membros. Filtro opcional: `?atribuicao=funcionário`.

O combo da tela de projeto lista apenas funcionários (estagiário não aparece para alocação).

### Relatório

**GET** `/soa/relatorios/projetos`

```json
{
  "porStatus": [
    { "status": "EM_ANALISE", "quantidade": 4, "orcamentoTotal": 0 }
  ],
  "mediaDuracaoDiasEncerrados": 12.5,
  "membrosUnicosAlocados": 3
}
```

`mediaDuracaoDiasEncerrados` fica `null` se não houver projeto encerrado com `dataInicio` e `dataRealTermino`.

## Testes

Suíte: `com.empresa.desafio.jcompanyqa.AppSuiteTest`

| Classe | O que cobre |
|---|---|
| `StatusProjetoTest` | sequência, cancelado, exclusão, projeto ativo |
| `MembroFuncionarioTest` | somente atribuição funcionário |
| `ProjetoRiscoTest` | baixo / médio / alto e ausência de dados |

No Eclipse: botão direito na classe ou na suíte → **Run As → JUnit Test**. Sem Tomcat.

A API está documentada neste README. Não há Swagger UI (Jaguar 6.1.5 / RESTEasy da suíte).
