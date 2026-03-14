package com.ynz.ai.rag.ragdemo.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response after document processing")
public class DocumentResponse {

    @Schema(description = "Unique document identifier")
    private String documentId;

    @Schema(description = "Original filename")
    private String filename;

    @Schema(description = "Number of chunks created from the document")
    private int chunksCreated;

    @Schema(description = "Status message")
    private String message;
}
