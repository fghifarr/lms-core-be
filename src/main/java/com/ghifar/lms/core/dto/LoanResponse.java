package com.ghifar.lms.core.dto;

import java.time.Instant;

public record LoanResponse(
        Book book,
        Borrower borrower,
        Instant borrowedAt,
        Instant returnedAt
) {
    public record Book(
            Long id,
            String isbn,
            String title
    ) {
    }

    public record Borrower(
            Integer id,
            String name,
            String email
    ) {
    }
}
