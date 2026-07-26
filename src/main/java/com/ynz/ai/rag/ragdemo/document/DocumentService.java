package com.ynz.ai.rag.ragdemo.document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final VectorStore vectorStore;
    private final TokenTextSplitter textSplitter;

    public DocumentResponse processDocument(MultipartFile file, String description) {
        try {
            log.info("Processing document: {}", file.getOriginalFilename());

            String content = extractContent(file);

            // Create document with metadata
            String documentId = UUID.randomUUID().toString();
            Document document = new Document(content, Map.of(
                    "filename", Objects.requireNonNull(file.getOriginalFilename()),
                    "documentId", documentId,
                    "description", description != null ? description : "",
                    "uploadTime", System.currentTimeMillis()
            ));

            // Split into chunks
            List<Document> chunks = textSplitter.split(document);
            log.info("Split document into {} chunks", chunks.size());

            // Store in vector database
            vectorStore.add(chunks);
            log.info("Successfully stored document in vector store");

            return DocumentResponse.builder()
                    .documentId(documentId)
                    .filename(file.getOriginalFilename())
                    .chunksCreated(chunks.size())
                    .message("Document processed successfully")
                    .build();

        } catch (IOException e) {
            log.error("Error processing document", e);
            throw new RuntimeException("Failed to process document", e);
        }
    }

    private String extractContent(MultipartFile file) throws IOException {
        if (isPdf(file)) {
            try (PDDocument document = Loader.loadPDF(file.getBytes())) {
                return new PDFTextStripper().getText(document);
            }
        }

        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }

    private boolean isPdf(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        return "application/pdf".equalsIgnoreCase(contentType)
                || (filename != null && filename.toLowerCase().endsWith(".pdf"));
    }

    public DocumentResponse processText(DocumentRequest request) {
        log.info("Processing text content");

        String documentId = UUID.randomUUID().toString();
        Document document = new Document(request.getContent(), Map.of(
                "documentId", documentId,
                "title", request.getTitle() != null ? request.getTitle() : "Untitled",
                "uploadTime", System.currentTimeMillis()
        ));

        List<Document> chunks = textSplitter.split(document);
        vectorStore.add(chunks);

        return DocumentResponse.builder()
                .documentId(documentId)
                .chunksCreated(chunks.size())
                .message("Text content processed successfully")
                .build();
    }

    public void deleteDocument(String documentId) {
        log.info("Deleting document: {}", documentId);
        // TODO: Implement document deletion from vector store
        // This depends on the vector store implementation
        log.warn("Document deletion not yet implemented");
    }
}
