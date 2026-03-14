package com.ynz.ai.rag.ragdemo.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Chat request containing user question")
public class ChatRequest {

    @Schema(description = "User's question", example = "What is RAG?")
    private String question;

    @Schema(description = "Number of relevant documents to retrieve", example = "3", defaultValue = "3")
    @Builder.Default
    private int topK = 3;

    @Schema(description = "Optional conversation ID for maintaining context")
    private String conversationId;
}
