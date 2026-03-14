package com.ynz.ai.rag.ragdemo.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "Chat and query endpoints for RAG system")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/query")
    @Operation(summary = "Send a query to the RAG system", description = "Query the knowledge base using RAG")
    public ResponseEntity<ChatResponse> query(@RequestBody ChatRequest request) {
        log.info("Received chat query: {}", request.getQuestion());
        ChatResponse response = chatService.chat(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stream")
    @Operation(summary = "Stream chat response", description = "Get streaming response from RAG system")
    public ResponseEntity<String> streamQuery(@RequestBody ChatRequest request) {
        log.info("Received streaming chat query: {}", request.getQuestion());
        // TODO: Implement streaming response
        return ResponseEntity.ok("Streaming not yet implemented");
    }
}
