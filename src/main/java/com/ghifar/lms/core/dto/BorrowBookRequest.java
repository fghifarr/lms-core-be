package com.ghifar.lms.core.dto;

import jakarta.validation.constraints.NotNull;

public record BorrowBookRequest(
        @NotNull
        Long bookId,
        @NotNull
        Integer borrowerId
) {
}
