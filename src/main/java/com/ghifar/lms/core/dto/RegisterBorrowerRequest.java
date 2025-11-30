package com.ghifar.lms.core.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterBorrowerRequest(
        @NotBlank
        String name,
        @NotBlank
        String email
) {
}
