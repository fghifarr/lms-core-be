package com.ghifar.lms.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to register a new borrower")
public record RegisterBorrowerRequest(
        @Schema(description = "The borrower's name", example = "Ghifar", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String name,
        @Schema(description = "The borrower's email", example = "fghifarr@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String email
) {
}
