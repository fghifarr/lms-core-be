package com.ghifar.lms.core.dto;

import java.util.List;

public record ErrorResponse(
        int status,
        String message,
        List<String> errors
) {
}
