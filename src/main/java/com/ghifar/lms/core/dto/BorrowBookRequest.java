package com.ghifar.lms.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to borrow the book")
public record BorrowBookRequest(
        @Schema(description = "the id of book (PK)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Long bookId,
        @Schema(description = "the id of borrower (PK)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Integer borrowerId
) {
}
