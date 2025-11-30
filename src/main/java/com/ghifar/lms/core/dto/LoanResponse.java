package com.ghifar.lms.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Response of loan details. Used by borrow and return book")
public record LoanResponse(
        @Schema(description = "the book object of LoanResponse", requiredMode = Schema.RequiredMode.REQUIRED)
        Book book,
        @Schema(description = "the borrower object of LoanResponse", requiredMode = Schema.RequiredMode.REQUIRED)
        Borrower borrower,
        @Schema(description = "the time the book is borrowed", example = "2025-11-30T13:24:50.438712Z", requiredMode = Schema.RequiredMode.REQUIRED)
        Instant borrowedAt,
        @Schema(description = "the time the book is returned", example = "2025-11-30T13:24:50.438712Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Instant returnedAt
) {
    @Schema(description = "the book object of LoanResponse")
    public record Book(
            @Schema(description = "the id of book (PK)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
            Long id,
            @Schema(description = "ISBN code", example = "978-1-56619-909-4", requiredMode = Schema.RequiredMode.REQUIRED)
            String isbn,
            @Schema(description = "the book title", example = "Catatan si Ghifar", requiredMode = Schema.RequiredMode.REQUIRED)
            String title
    ) {
    }

    @Schema(description = "the borrower object of LoanResponse")
    public record Borrower(
            Integer id,
            String name,
            String email
    ) {
    }
}
