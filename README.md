# RAG Demo - Spring AI Application

A Retrieval-Augmented Generation (RAG) demo application built with Spring Boot and Spring AI.

## 🏗️ Project Structure (Feature-Based)

```
src/main/java/com/ynz/ai/rag/ragdemo/
├── RagDemoApplication.java          # Main application entry point
│
├── chat/                             # Chat/Query Feature
│   ├── ChatController.java          # REST endpoints for chat
│   ├── ChatService.java             # RAG orchestration logic
│   ├── ChatRequest.java             # Request DTO
│   └── ChatResponse.java            # Response DTO
│
├── document/                         # Document Management Feature
│   ├── DocumentController.java      # Document upload endpoints
│   ├── DocumentService.java         # Document processing logic
│   ├── DocumentRequest.java         # Request DTO
│   └── DocumentResponse.java        # Response DTO
│
├── config/                           # Configuration
│   ├── VectorStoreConfig.java       # Vector store & text splitter config
│   └── SwaggerConfig.java           # API documentation config
│
└── common/                           # Shared Components
    └── exception/
        ├── RagException.java        # Custom exception
        └── GlobalExceptionHandler.java  # Global error handling
```

## 🚀 Getting Started

### Prerequisites

1. **Java 21** installed
2. **Maven** installed
3. **OpenAI API Key** - Set as environment variable `OPENAI_API_KEY`
4. **Chroma Vector Database** running locally

### Setup Chroma Vector Database

**⚠️ Important Note on ChromaDB Version**

The current version of Spring AI used in this project requires a specific version of ChromaDB to function correctly. Using a different version, such as `latest` or newer images from `chromadb/chroma`, may cause connection issues.

Please use the following Docker image and command:

```bash
docker run -d -p 8000:8000 ghcr.io/chroma-core/chroma:1.0.0
```

The `docker-compose.yml` file in this project is also configured to use a compatible version. You can run it with:
```bash
docker-compose up -d
```

### Create collection 

```bash
curl -X POST http://localhost:8000/api/v2/tenants/default_tenant/databases/default_database/collections -H "Content-Type: application/json" -d "{\"name\":\"demo_collection\"}"
```
then verify collection is created:
```bash
curl http://localhost:8000/api/v2/tenants/default_tenant/databases/default_database/collections
```

### Set Environment Variable

**Windows (PowerShell):**
```powershell
$env:OPENAI_API_KEY="your-api-key-here"
```

**Windows (Command Prompt):**
```cmd
set OPENAI_API_KEY=your-api-key-here
```

**Linux/Mac:**
```bash
export OPENAI_API_KEY=your-api-key-here
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the Swagger UI at:

**http://localhost:8080/swagger-ui.html**

## 🔧 API Endpoints

### Chat Endpoints

- **POST** `/api/chat/query` - Send a query to the RAG system
- **POST** `/api/chat/stream` - Stream chat response (TODO)

### Document Endpoints

- **POST** `/api/documents/upload` - Upload a document (file)
- **POST** `/api/documents/text` - Add text content directly
- **DELETE** `/api/documents/{documentId}` - Delete a document

## 📝 Example Usage

### 1. Add Text Content

```bash
curl -X POST http://localhost:8080/api/documents/text \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Spring AI is a framework for building AI applications with Spring Boot. It provides abstractions for working with various AI models and vector stores.",
    "title": "Spring AI Introduction"
  }'
```

### 2. Query the Knowledge Base

```bash
curl -X POST http://localhost:8080/api/chat/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "What is Spring AI?",
    "topK": 3
  }'
```

### 3. Upload a Document

```bash
curl -X POST http://localhost:8080/api/documents/upload \
  -F "file=@/path/to/your/document.txt" \
  -F "description=Technical documentation"
```

## ⚙️ Configuration

Edit `src/main/resources/application.properties`:

```properties
# OpenAI Configuration
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.7

# Chroma Vector Store
spring.ai.vectorstore.chroma.client.host=localhost
spring.ai.vectorstore.chroma.client.port=8000
spring.ai.vectorstore.chroma.collection-name=rag-demo-collection
```

## 🛠️ Technology Stack

- **Spring Boot 3.5.11** - Application framework
- **Spring AI 1.1.2** - AI integration framework
- **OpenAI GPT-4o-mini** - Language model
- **Chroma** - Vector database
- **Lombok** - Reduce boilerplate code
- **Swagger/OpenAPI** - API documentation

## 📖 How RAG Works in This Application

1. **Document Ingestion** (`DocumentService`)
   - Upload documents or text
   - Split into chunks using `TokenTextSplitter`
   - Generate embeddings
   - Store in Chroma vector database

2. **Query Processing** (`ChatService`)
   - User sends a question
   - Retrieve relevant document chunks from vector store
   - Build context from retrieved documents
   - Send context + question to OpenAI
   - Return AI-generated answer with sources

## 🔍 Next Steps

- [ ] Implement streaming responses
- [ ] Add conversation history/memory
- [ ] Support PDF document uploads
- [ ] Add authentication/authorization
- [ ] Implement document deletion from vector store
- [ ] Add metrics and monitoring
- [ ] Create frontend UI

## 📄 License

This project is licensed under the **MIT License**.

```
MIT License

Copyright (c) 2026 ynz

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

> This is a demo project intended for learning and experimentation purposes. Feel free to use, modify, and distribute it as you see fit.
