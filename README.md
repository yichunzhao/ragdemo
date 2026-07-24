# RAG Demo - Spring AI Application

A Retrieval-Augmented Generation (RAG) demo application built with Spring Boot and Spring AI.

The current version uses:

- Spring Boot 4.1.0
- Spring AI 2.0.0
- Ollama for local chat and embedding models
- PostgreSQL with the PgVector extension for persistent vector storage
- Docker Compose for the local AI/database infrastructure, started automatically by Spring Boot during development runs

## Project Structure

```text
src/main/java/com/ynz/ai/rag/ragdemo/
├── RagDemoApplication.java
├── chat/                  # Chat/query endpoints and RAG orchestration
├── document/              # Document upload and ingestion
├── config/                # Text splitter and Swagger config
└── common/exception/      # Shared error handling
```

## Prerequisites

1. Java 21
2. Maven
3. Docker Desktop

No OpenAI API key is required for this setup.

## Run the Application

```bash
mvn spring-boot:run
```

Spring Boot uses the `spring-boot-docker-compose` runtime dependency to start the services declared in `docker-compose.yml`.
Docker Desktop must be running, but you do not need to run `docker compose up -d` manually before starting the app.

The compose file provides:

- `rag-demo-ollama` on `localhost:11434`
- `rag-demo-pgvector` on `localhost:5432`
- `rag-demo-ollama-models`, a one-shot helper that pulls:
  - `llama3.2:1b` for chat
  - `nomic-embed-text` for embeddings

The first startup can take a while because Ollama downloads the models.

The application starts on `http://localhost:8080`.

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## Build the Project

```bash
mvn clean install
```

You can also run `RagDemoApplication.java` from IntelliJ IDEA. The same Docker Compose integration applies as long as Docker Desktop is running.

## Manual Docker Commands

Normally these are not required before `mvn spring-boot:run`, but they are useful for inspection and cleanup:

```bash
docker compose ps
docker compose logs -f ollama
docker compose logs -f postgres
docker compose down
```

## Configuration

Key settings live in `src/main/resources/application.properties`:

```properties
spring.ai.model.chat=ollama
spring.ai.model.embedding=ollama
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=llama3.2:1b
spring.ai.ollama.embedding.model=nomic-embed-text

spring.datasource.url=jdbc:postgresql://localhost:5432/ragdemo
spring.datasource.username=ragdemo
spring.datasource.password=ragdemo
spring.ai.vectorstore.pgvector.initialize-schema=true
spring.ai.vectorstore.pgvector.dimensions=768
spring.docker.compose.lifecycle-management=start-only
```

`nomic-embed-text` produces 768-dimensional embeddings, so PgVector is configured with `dimensions=768`.
`spring.docker.compose.lifecycle-management=start-only` lets Spring Boot start missing Compose services without stopping them when the app exits.

## API Endpoints

- `POST /api/documents/text` - Add text content directly
- `POST /api/documents/upload` - Upload a text document file
- `POST /api/chat/query` - Ask a RAG question
- `POST /api/chat/stream` - Stream chat response
- `DELETE /api/documents/{documentId}` - Delete a document

## Example Usage

### Add Text Content

```bash
curl -X POST http://localhost:8080/api/documents/text \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Spring AI is a framework for building AI applications with Spring Boot. It supports local models through Ollama and vector stores such as PgVector.",
    "title": "Spring AI Local RAG"
  }'
```

### Query the Knowledge Base

```bash
curl -X POST http://localhost:8080/api/chat/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "What does this project use for local RAG?",
    "topK": 3
  }'
```

## How RAG Works

1. Documents are uploaded as text or files.
2. `TokenTextSplitter` splits content into chunks.
3. Ollama `nomic-embed-text` creates embeddings.
4. PgVector stores the chunks and vectors in PostgreSQL.
5. A user question is embedded and searched against PgVector.
6. Retrieved chunks are inserted into the prompt.
7. Ollama `llama3.2:1b` generates the answer.

To remove persisted vectors and downloaded models:

```bash
docker compose down -v
```
