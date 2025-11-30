package com.ghifar.lms.core.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterBookRequest(
        @NotBlank
        String title,
        @NotBlank
        String isbn,
        @NotBlank
        String author
) {
}
