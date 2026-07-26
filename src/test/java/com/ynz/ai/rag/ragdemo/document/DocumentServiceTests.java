package com.ynz.ai.rag.ragdemo.document;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.nio.charset.StandardCharsets;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.mock.web.MockMultipartFile;

class DocumentServiceTests {

    private final VectorStore vectorStore = mock(VectorStore.class);
    private final TokenTextSplitter textSplitter = mock(TokenTextSplitter.class);
    private final DocumentService documentService = new DocumentService(vectorStore, textSplitter);

    @Test
    void processesUtf8TextUpload() {
        when(textSplitter.split(any(Document.class)))
                .thenAnswer(invocation -> Collections.singletonList(invocation.getArgument(0, Document.class)));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "notes.txt",
                "text/plain",
                "Spring AI stores chunks in PgVector.".getBytes(StandardCharsets.UTF_8));

        DocumentResponse response = documentService.processDocument(file, "Text notes");

        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(textSplitter).split(documentCaptor.capture());
        verify(vectorStore).add(any());
        assertThat(documentCaptor.getValue().getText()).contains("Spring AI stores chunks");
        assertThat(response.getFilename()).isEqualTo("notes.txt");
        assertThat(response.getChunksCreated()).isEqualTo(1);
    }

    @Test
    void extractsTextFromPdfUpload() throws Exception {
        when(textSplitter.split(any(Document.class)))
                .thenAnswer(invocation -> Collections.singletonList(invocation.getArgument(0, Document.class)));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "notes.pdf",
                "application/pdf",
                pdfBytes("PDF ingestion works with PDFBox."));

        DocumentResponse response = documentService.processDocument(file, "PDF notes");

        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(textSplitter).split(documentCaptor.capture());
        verify(vectorStore).add(any());
        assertThat(documentCaptor.getValue().getText()).contains("PDF ingestion works");
        assertThat(response.getFilename()).isEqualTo("notes.pdf");
        assertThat(response.getChunksCreated()).isEqualTo(1);
    }

    private byte[] pdfBytes(String text) throws Exception {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.newLineAtOffset(72, 720);
                contentStream.showText(text);
                contentStream.endText();
            }
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }
}
