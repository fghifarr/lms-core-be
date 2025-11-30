package com.ghifar.lms.core.dto;

public record BookResponse(
        Long id,
        String title,
        String isbn,
        String author,
        String status
) {
}
