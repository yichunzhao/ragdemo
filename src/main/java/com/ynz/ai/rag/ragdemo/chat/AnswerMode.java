package com.ynz.ai.rag.ragdemo.chat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "How the answer was generated")
public enum AnswerMode {
    RAG,
    MODEL_KNOWLEDGE
}
