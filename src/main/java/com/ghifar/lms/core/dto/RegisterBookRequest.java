package com.ghifar.lms.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to register a new book")
public record RegisterBookRequest(
        @Schema(description = "the book title", example = "Catatan si Ghifar", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String title,
        @Schema(description = "ISBN code", example = "978-1-56619-909-4", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String isbn,
        @Schema(description = "The book's author", example = "F Ghifar R", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String author
) {
}
