package com.ynz.ai.rag.ragdemo.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        AnswerMode answerMode = relevantDocs.isEmpty() ? AnswerMode.MODEL_KNOWLEDGE : AnswerMode.RAG;

        // Step 2: Create prompt with RAG context or model-knowledge fallback instructions
        String prompt = buildPrompt(request.getQuestion(), relevantDocs, answerMode);

        // Step 3: Call ChatModel
        String answer = chatModel.call(prompt);

        log.info("Generated {} answer for question: {}", answerMode, request.getQuestion());

        return ChatResponse.builder()
                .answer(answer)
                .question(request.getQuestion())
                .sources(relevantDocs)
                .answerMode(answerMode)
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
        StringBuilder context = new StringBuilder("Context:\n");
        for (int i = 0; i < documents.size(); i++) {
            context.append(String.format("[%d] %s\n\n", i + 1, documents.get(i)));
        }
        return context.toString();
    }

    private String buildPrompt(String question, List<String> documents, AnswerMode answerMode) {
        if (answerMode == AnswerMode.MODEL_KNOWLEDGE) {
            return String.format("""
                    You are a helpful AI assistant.
                    No knowledge-base context was found for this question.
                    Answer from your general model knowledge.
                    Be clear that no RAG sources were found.

                    Question: %s

                    Answer:
                    """, question);
        }

        return String.format("""
                You are a helpful AI assistant.
                Answer the user's question using the provided knowledge-base context.
                If the context is insufficient, say what is missing instead of inventing details.

                %s

                Question: %s
                Answer:
                """, buildContext(documents), question);
    }
}
