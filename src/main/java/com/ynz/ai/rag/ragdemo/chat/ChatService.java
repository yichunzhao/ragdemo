package com.ynz.ai.rag.ragdemo.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    public ChatResponse chat(ChatRequest request) {
        log.info("Processing chat request: {}", request.getQuestion());

        // Step 1: Retrieve relevant documents from vector store
        List<String> relevantDocs = retrieveRelevantDocuments(request.getQuestion(), request.getTopK());

        // Step 2: Build context from retrieved documents
        String context = buildContext(relevantDocs);

        // Step 3: Create prompt with context
        String prompt = buildPrompt(request.getQuestion(), context);

        // Step 4: Call ChatModel
        String answer = ChatClient.create(chatModel)
                .prompt()
                .user(prompt)
                .call()
                .content();

        log.info("Generated answer for question: {}", request.getQuestion());

        return ChatResponse.builder()
                .answer(answer)
                .question(request.getQuestion())
                .sources(relevantDocs)
                .build();
    }

    private List<String> retrieveRelevantDocuments(String question, int topK) {
        log.debug("Retrieving top {} relevant documents for question", topK);

        var searchRequest = SearchRequest.builder()
                .query(question)
                .topK(topK)
                .build();
        var results = vectorStore.similaritySearch(searchRequest);

        return results.stream()
                .map(Document::getText)
                .collect(Collectors.toList());
    }

    private String buildContext(List<String> documents) {
        if (documents.isEmpty()) {
            return "No relevant context found.";
        }

        StringBuilder context = new StringBuilder("Context:\n");
        for (int i = 0; i < documents.size(); i++) {
            context.append(String.format("[%d] %s\n\n", i + 1, documents.get(i)));
        }
        return context.toString();
    }

    private String buildPrompt(String question, String context) {
        return String.format("""
                You are a helpful AI assistant. Answer the user's question based on the provided context.
                If the context doesn't contain relevant information, say so politely.

                %s

                Question: %s

                Answer:
                """, context, question);
    }
}
