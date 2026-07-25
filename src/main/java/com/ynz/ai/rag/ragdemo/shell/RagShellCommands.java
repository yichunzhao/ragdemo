package com.ynz.ai.rag.ragdemo.shell;

import com.ynz.ai.rag.ragdemo.chat.ChatRequest;
import com.ynz.ai.rag.ragdemo.chat.ChatResponse;
import com.ynz.ai.rag.ragdemo.chat.ChatService;
import com.ynz.ai.rag.ragdemo.document.DocumentRequest;
import com.ynz.ai.rag.ragdemo.document.DocumentResponse;
import com.ynz.ai.rag.ragdemo.document.DocumentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.core.command.annotation.Argument;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RagShellCommands {

    private final ChatService chatService;
    private final DocumentService documentService;

    @Command(name = "ask", description = "Ask a RAG question. Use quotes around questions with spaces.")
    public String ask(
            @Argument(index = 0, description = "Question to ask the RAG pipeline") String question,
            @Option(longName = "top-k", defaultValue = "3",
                    description = "Number of matching chunks to retrieve") int topK) {

        ChatResponse response = chatService.chat(ChatRequest.builder()
                .question(question)
                .topK(topK)
                .build());

        return formatChatResponse(response);
    }

    @Command(name = "ingest-text", description = "Add text content to the RAG knowledge base.")
    public String ingestText(
            @Argument(index = 0, description = "Text content to add. Use quotes around text with spaces.") String content,
            @Option(longName = "title", defaultValue = "Shell input", description = "Title for the content") String title) {

        DocumentResponse response = documentService.processText(DocumentRequest.builder()
                .content(content)
                .title(title)
                .build());

        return String.format("""
                Ingested: %s
                Document ID: %s
                Chunks created: %d
                """, response.getMessage(), response.getDocumentId(), response.getChunksCreated());
    }

    private String formatChatResponse(ChatResponse response) {
        StringBuilder output = new StringBuilder();
        output.append("Answer:\n")
                .append(response.getAnswer())
                .append("\n");

        List<String> sources = response.getSources();
        if (sources != null && !sources.isEmpty()) {
            output.append("\nSources:\n");
            for (int i = 0; i < sources.size(); i++) {
                output.append("[").append(i + 1).append("] ")
                        .append(sources.get(i))
                        .append("\n");
            }
        }

        return output.toString();
    }
}
