package com.ynz.ai.rag.ragdemo.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

class ChatServiceTests {

    private final ChatModel chatModel = mock(ChatModel.class);
    private final VectorStore vectorStore = mock(VectorStore.class);
    private final ChatService chatService = new ChatService(chatModel, vectorStore);

    @Test
    void usesRagModeWhenVectorStoreReturnsDocuments() {
        when(vectorStore.similaritySearch(any(SearchRequest.class)))
                .thenReturn(List.of(new Document("PgVector stores embeddings.")));
        when(chatModel.call(any(String.class))).thenReturn("The project uses PgVector.");

        ChatResponse response = chatService.chat(ChatRequest.builder()
                .question("What stores embeddings?")
                .topK(3)
                .build());

        assertThat(response.getAnswerMode()).isEqualTo(AnswerMode.RAG);
        assertThat(response.getSources()).containsExactly("PgVector stores embeddings.");
        assertThat(response.getAnswer()).isEqualTo("The project uses PgVector.");
    }

    @Test
    void fallsBackToModelKnowledgeWhenVectorStoreIsEmpty() {
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of());
        when(chatModel.call(any(String.class))).thenReturn("No RAG sources were found. RAG means retrieval-augmented generation.");

        ChatResponse response = chatService.chat(ChatRequest.builder()
                .question("What is RAG?")
                .topK(3)
                .build());

        assertThat(response.getAnswerMode()).isEqualTo(AnswerMode.MODEL_KNOWLEDGE);
        assertThat(response.getSources()).isEmpty();
        assertThat(response.getAnswer()).contains("No RAG sources were found");
    }
}
