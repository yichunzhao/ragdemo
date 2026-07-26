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
@Schema(description = "Request to add text content to knowledge base")
public class DocumentRequest {

    @Schema(description = "Text content to add", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "Title or description of the content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String title;

    @Schema(description = "Additional metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String metadata;
}
