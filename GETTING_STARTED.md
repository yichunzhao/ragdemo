# Getting Started with RAG Demo

This project now demonstrates a fully local RAG stack:

- Spring Boot 4.1.0 and Spring AI 2.0.0 run the application.
- Ollama runs the chat and embedding models.
- PgVector stores embeddings persistently in PostgreSQL.
- Docker Compose starts both services.

## Quick Start

### 1. Run the Application

```bash
mvn spring-boot:run
```

Spring Boot starts the Docker Compose services automatically.
The first run downloads `llama3.2:1b` and `nomic-embed-text`, so it can take a few minutes.

Check service status:

```bash
docker compose ps
```

### 2. Build the Project

```bash
mvn clean install
```

You can also run `RagDemoApplication.java` from IntelliJ IDEA.

### 3. Open Swagger UI

```text
http://localhost:8080/swagger-ui.html
```

## Test the Application

### Add Knowledge

```bash
curl -X POST http://localhost:8080/api/documents/text \
  -H "Content-Type: application/json" \
  -d '{
    "content": "This RAG demo uses Ollama for local LLM inference and PgVector for persistent vector search.",
    "title": "Local RAG Stack"
  }'
```

### Ask a Question

```bash
curl -X POST http://localhost:8080/api/chat/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "What does the local RAG demo use?",
    "topK": 3
  }'
```

## Pipeline

```text
Upload Document -> Split into Chunks -> Embed with Ollama -> Store in PgVector
User Question -> Embed with Ollama -> Search PgVector -> Build Context -> Generate with Ollama
```

## Troubleshooting

### Ollama model is missing

Run:

```bash
docker compose run --rm ollama-models
```

### PgVector table or schema issue

The app is configured with:

```properties
spring.ai.vectorstore.pgvector.initialize-schema=true
```

If you changed embedding dimensions, reset the database volume:

```bash
docker compose down -v
docker compose up -d
```

### Docker services are not reachable

Check:

```bash
docker compose ps
docker compose logs -f
```
