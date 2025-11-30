package com.ghifar.lms.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response of borrower registration")
public record RegisterBorrowerResponse(
        @Schema(description = "The borrower's name", example = "Ghifar", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,
        @Schema(description = "The borrower's email", example = "fghifarr@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email
) {
}
