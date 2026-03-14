package com.ynz.ai.rag.ragdemo.document;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "Document management endpoints")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    @Operation(summary = "Upload a document", description = "Upload and process a document for RAG")
    public ResponseEntity<DocumentResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description) {

        log.info("Received document upload: {}", file.getOriginalFilename());
        DocumentResponse response = documentService.processDocument(file, description);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/text")
    @Operation(summary = "Add text content", description = "Add text content directly to the knowledge base")
    public ResponseEntity<DocumentResponse> addText(@RequestBody DocumentRequest request) {
        log.info("Received text content to add");
        DocumentResponse response = documentService.processText(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{documentId}")
    @Operation(summary = "Delete a document", description = "Remove a document from the knowledge base")
    public ResponseEntity<Void> deleteDocument(@PathVariable String documentId) {
        log.info("Deleting document: {}", documentId);
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}
