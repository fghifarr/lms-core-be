package com.ghifar.lms.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response of book details. Used by Register and get list book")
public record BookResponse(
        @Schema(description = "the id of book (PK)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "the book title", example = "Catatan si Ghifar", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,
        @Schema(description = "ISBN code", example = "978-1-56619-909-4", requiredMode = Schema.RequiredMode.REQUIRED)
        String isbn,
        @Schema(description = "The book's author", example = "F Ghifar R", requiredMode = Schema.RequiredMode.REQUIRED)
        String author,
        @Schema(description = "The book status. Enums name of Book.Status", example = "AVAILABLE", requiredMode = Schema.RequiredMode.REQUIRED)
        String status
) {
}
