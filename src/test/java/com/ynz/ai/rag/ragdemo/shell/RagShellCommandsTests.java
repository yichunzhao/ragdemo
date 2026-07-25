package com.ynz.ai.rag.ragdemo.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ynz.ai.rag.ragdemo.chat.ChatResponse;
import com.ynz.ai.rag.ragdemo.chat.ChatService;
import com.ynz.ai.rag.ragdemo.document.DocumentResponse;
import com.ynz.ai.rag.ragdemo.document.DocumentService;
import java.util.List;
import org.junit.jupiter.api.Test;

class RagShellCommandsTests {

    private final ChatService chatService = mock(ChatService.class);
    private final DocumentService documentService = mock(DocumentService.class);
    private final RagShellCommands commands = new RagShellCommands(chatService, documentService);

    @Test
    void askReturnsAnswerAndSources() {
        when(chatService.chat(any())).thenReturn(ChatResponse.builder()
                .question("What is RAG?")
                .answer("RAG combines retrieval and generation.")
                .sources(List.of("Retrieval-Augmented Generation source text"))
                .build());

        String output = commands.ask("What is RAG?", 3);

        assertThat(output)
                .contains("Answer:")
                .contains("RAG combines retrieval and generation.")
                .contains("Sources:")
                .contains("Retrieval-Augmented Generation source text");
        verify(chatService).chat(any());
    }

    @Test
    void ingestTextReturnsDocumentSummary() {
        when(documentService.processText(any())).thenReturn(DocumentResponse.builder()
                .message("Text content processed successfully")
                .documentId("doc-123")
                .chunksCreated(2)
                .build());

        String output = commands.ingestText("Spring AI supports local RAG.", "Demo");

        assertThat(output)
                .contains("Text content processed successfully")
                .contains("doc-123")
                .contains("Chunks created: 2");
        verify(documentService).processText(any());
    }
}
