# Getting Started with RAG Demo

## ✅ What's Been Set Up

Your RAG demo project is now fully structured with a **feature-based architecture**. Here's what you have:

### 📁 Project Structure

```
src/main/java/com/ynz/ai/rag/ragdemo/
├── RagDemoApplication.java
├── chat/                    ✅ Chat/Query Feature
│   ├── ChatController.java
│   ├── ChatService.java
│   ├── ChatRequest.java
│   └── ChatResponse.java
├── document/                ✅ Document Management Feature
│   ├── DocumentController.java
│   ├── DocumentService.java
│   ├── DocumentRequest.java
│   └── DocumentResponse.java
├── config/                  ✅ Configuration
│   ├── VectorStoreConfig.java
│   └── SwaggerConfig.java
└── common/                  ✅ Shared Components
    └── exception/
        ├── RagException.java
        └── GlobalExceptionHandler.java
```

### 🔧 Configuration

Your `application.properties` is configured with:
- ✅ OpenAI API key mapping to `${OPENAI_API_KEY}` environment variable
- ✅ GPT-4o-mini model configuration
- ✅ Chroma vector store settings
- ✅ Logging configuration

### 🐳 Docker Setup

A `docker-compose.yml` file is ready to start Chroma vector database.

## 🚀 Quick Start Guide

### Step 1: Start Chroma Vector Database

```bash
docker-compose up -d
```

Verify Chroma is running:
```bash
curl http://localhost:8000/api/v1/heartbeat
```

### Step 2: Verify Environment Variable

Make sure your OpenAI API key is set:

**PowerShell:**
```powershell
echo $env:OPENAI_API_KEY
```

If not set, add it:
```powershell
$env:OPENAI_API_KEY="sk-your-api-key-here"
```

**For permanent setup (Windows):**
1. Search for "Environment Variables" in Windows
2. Add `OPENAI_API_KEY` as a system or user variable
3. Restart your IDE

### Step 3: Build the Project

```bash
mvn clean install
```

### Step 4: Run the Application

```bash
mvn spring-boot:run
```

Or run from IntelliJ IDEA:
- Right-click on `RagDemoApplication.java`
- Select "Run 'RagDemoApplication'"

### Step 5: Access Swagger UI

Open your browser and go to:
```
http://localhost:8080/swagger-ui.html
```

## 📝 Test the Application

### Test 1: Add Knowledge to the System

```bash
curl -X POST http://localhost:8080/api/documents/text \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Spring AI is a framework that provides abstractions for integrating AI models into Spring Boot applications. It supports OpenAI, Azure OpenAI, Anthropic Claude, and many other providers. Spring AI also includes vector store integrations for RAG applications.",
    "title": "Spring AI Overview"
  }'
```

### Test 2: Query the Knowledge Base

```bash
curl -X POST http://localhost:8080/api/chat/query \
  -H "Content-Type: application/json" \
  -d '{
    "question": "What is Spring AI and what providers does it support?",
    "topK": 3
  }'
```

Expected response:
```json
{
  "answer": "Spring AI is a framework that provides abstractions for integrating AI models into Spring Boot applications. It supports various providers including OpenAI, Azure OpenAI, and Anthropic Claude, among others. It also includes vector store integrations for RAG (Retrieval-Augmented Generation) applications.",
  "question": "What is Spring AI and what providers does it support?",
  "sources": [
    "Spring AI is a framework that provides abstractions..."
  ]
}
```

### Test 3: Upload a Text File

Create a file `sample.txt`:
```
Retrieval-Augmented Generation (RAG) is a technique that combines
information retrieval with text generation. It retrieves relevant
documents from a knowledge base and uses them as context for
generating accurate responses.
```

Upload it:
```bash
curl -X POST http://localhost:8080/api/documents/upload \
  -F "file=@sample.txt" \
  -F "description=RAG explanation"
```

## 🎯 Key Features Implemented

### Chat Feature (`/chat`)
- ✅ Query endpoint with RAG support
- ✅ Retrieves relevant documents from vector store
- ✅ Builds context and sends to OpenAI
- ✅ Returns answer with source documents

### Document Feature (`/document`)
- ✅ Upload text files
- ✅ Add text content directly
- ✅ Automatic text chunking
- ✅ Vector embedding and storage

### Configuration
- ✅ Vector store configuration
- ✅ Text splitter with optimal chunk size
- ✅ Swagger/OpenAPI documentation

### Error Handling
- ✅ Global exception handler
- ✅ Custom RAG exception
- ✅ Proper error responses

## 🔍 How It Works

1. **Document Ingestion Flow:**
   ```
   Upload Document → Split into Chunks → Generate Embeddings → Store in Chroma
   ```

2. **Query Flow:**
   ```
   User Question → Search Vector Store → Retrieve Relevant Chunks →
   Build Context → Send to OpenAI → Return Answer + Sources
   ```

## 📚 Next Steps

### Immediate Enhancements
- [ ] Add PDF document support
- [ ] Implement streaming responses
- [ ] Add conversation memory/history
- [ ] Create a simple web UI

### Advanced Features
- [ ] Multi-modal support (images, audio)
- [ ] Fine-tune chunk size and overlap
- [ ] Add metadata filtering
- [ ] Implement hybrid search (keyword + semantic)
- [ ] Add authentication/authorization
- [ ] Metrics and monitoring

## 🐛 Troubleshooting

### Issue: "Connection refused" to Chroma
**Solution:** Make sure Chroma is running:
```bash
docker ps | grep chroma
```

### Issue: "Invalid API key"
**Solution:** Check your environment variable:
```bash
echo $env:OPENAI_API_KEY  # PowerShell
```

### Issue: "No documents found"
**Solution:** Make sure you've uploaded documents first using the `/api/documents/text` or `/api/documents/upload` endpoints.

## 📖 Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [OpenAI API Documentation](https://platform.openai.com/docs)
- [Chroma Documentation](https://docs.trychroma.com/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)

## 💡 Tips

1. **Chunk Size:** The default chunk size is 800 tokens with 200 overlap. Adjust in `VectorStoreConfig.java` based on your use case.

2. **Model Selection:** Using `gpt-4o-mini` for cost efficiency. Switch to `gpt-4o` for better quality in `application.properties`.

3. **Top-K:** Default is 3 documents. Increase for more context, decrease for faster responses.

4. **Temperature:** Set to 0.7 for balanced creativity. Lower (0.1-0.3) for factual responses, higher (0.8-1.0) for creative responses.

---

**Happy Building! 🚀**
