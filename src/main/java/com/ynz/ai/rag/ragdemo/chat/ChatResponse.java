package com.ynz.ai.rag.ragdemo.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Chat response with answer and sources")
public class ChatResponse {

    @Schema(description = "AI-generated answer")
    private String answer;

    @Schema(description = "Original question")
    private String question;

    @Schema(description = "Source documents used to generate the answer")
    private List<String> sources;

    @Schema(description = "Conversation ID")
    private String conversationId;
}
