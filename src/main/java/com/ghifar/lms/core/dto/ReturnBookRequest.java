package com.ghifar.lms.core.dto;

import jakarta.validation.constraints.NotNull;

public record ReturnBookRequest(
        @NotNull
        Long bookId,
        @NotNull
        Integer borrowerId
) {
}
