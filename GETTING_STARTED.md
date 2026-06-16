# Getting Started with RAG Demo

This project is configured for a no-Docker demo flow. It uses Spring AI's in-memory `SimpleVectorStore`, so you only need Java, Maven, and an OpenAI API key.

## Quick Start

### 1. Verify Environment Variable

PowerShell:

```powershell
echo $env:OPENAI_API_KEY
```

If it is missing:

```powershell
$env:OPENAI_API_KEY="sk-your-api-key-here"
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or run `RagDemoApplication.java` from IntelliJ IDEA.

### 4. Open Swagger UI

```text
http://localhost:8080/swagger-ui.html
```

## Test the Application

### Add Knowledge

```bash
curl -X POST http://localhost:8080/api/documents/text \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Spring AI is a framework that provides abstractions for integrating AI models into Spring Boot applications. It supports OpenAI and many other providers. Spring AI also includes vector store integrations for RAG applications.",
    "title": "Spring AI Overview"
  }'
```

### Query the Knowledge Base

```bash
curl -X POST http://localhost:8080/api/chat/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "What is Spring AI and what providers does it support?",
    "topK": 3
  }'
```

### Upload a Text File

Create `sample.txt`:

```text
Retrieval-Augmented Generation (RAG) combines information retrieval with text generation. It retrieves relevant documents from a knowledge base and uses them as context for generating accurate responses.
```

Upload it:

```bash
curl -X POST http://localhost:8080/api/documents/upload \
  -F "file=@sample.txt" \
  -F "description=RAG explanation"
```

## How It Works

```text
Upload Document -> Split into Chunks -> Generate Embeddings -> Store in SimpleVectorStore
User Question -> Search Vector Store -> Retrieve Chunks -> Build Context -> Send to OpenAI
```

## Troubleshooting

### Documents disappear after restart

That is expected. `SimpleVectorStore` is in-memory, so uploaded content is cleared when the app stops. Upload the demo documents again after each restart.

### Invalid API key

Check your environment variable:

```powershell
echo $env:OPENAI_API_KEY
```

### No documents found

Add content first using `/api/documents/text` or `/api/documents/upload`, then query again.

## Tips

- Adjust chunk size and overlap in `VectorStoreConfig.java`.
- Use `gpt-4o-mini` for cost-efficient demos.
- Increase `topK` for more retrieved context, or decrease it for faster responses.
