# RAG Demo - Spring AI Application

A Retrieval-Augmented Generation (RAG) demo application built with Spring Boot and Spring AI.

## Project Structure

```text
src/main/java/com/ynz/ai/rag/ragdemo/
├── RagDemoApplication.java
├── chat/                  # Chat/query endpoints and RAG orchestration
├── document/              # Document upload and ingestion
├── config/                # Vector store, text splitter, and Swagger config
└── common/exception/      # Shared error handling
```

## Getting Started

### Prerequisites

1. Java 21
2. Maven
3. OpenAI API key set as `OPENAI_API_KEY`

No Docker or external vector database is required. This demo uses Spring AI's in-memory `SimpleVectorStore`, configured in `VectorStoreConfig`.

### Set Environment Variable

PowerShell:

```powershell
$env:OPENAI_API_KEY="your-api-key-here"
```

Command Prompt:

```cmd
set OPENAI_API_KEY=your-api-key-here
```

Linux/macOS:

```bash
export OPENAI_API_KEY=your-api-key-here
```

### Run the Application

```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

## API Endpoints

- `POST /api/chat/query` - Send a query to the RAG system
- `POST /api/chat/stream` - Stream chat response
- `POST /api/documents/upload` - Upload a document file
- `POST /api/documents/text` - Add text content directly
- `DELETE /api/documents/{documentId}` - Delete a document

## Example Usage

### Add Text Content

```bash
curl -X POST http://localhost:8080/api/documents/text \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Spring AI is a framework for building AI applications with Spring Boot. It provides abstractions for working with various AI models and vector stores.",
    "title": "Spring AI Introduction"
  }'
```

### Query the Knowledge Base

```bash
curl -X POST http://localhost:8080/api/chat/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "What is Spring AI?",
    "topK": 3
  }'
```

### Upload a Document

```bash
curl -X POST http://localhost:8080/api/documents/upload \
  -F "file=@/path/to/your/document.txt" \
  -F "description=Technical documentation"
```

## Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.7
```

`SimpleVectorStore` is in-memory. Uploaded documents are cleared when the application restarts, which keeps the project simple for demos.

## Technology Stack

- Spring Boot 3.5.14
- Spring AI 1.1.7
- OpenAI GPT-4o-mini
- Spring AI `SimpleVectorStore`
- Lombok
- Swagger/OpenAPI

## How RAG Works

1. Document ingestion splits uploaded text into chunks.
2. The embedding model converts chunks into vectors.
3. `SimpleVectorStore` stores vectors in memory.
4. Chat queries search the vector store for relevant chunks.
5. The chat model receives the retrieved context and generates an answer.

## Notes

This is a demo project for learning and experimentation. Use a persistent vector database such as Chroma, pgvector, Qdrant, Milvus, Weaviate, or Pinecone when you need stored documents to survive restarts.
