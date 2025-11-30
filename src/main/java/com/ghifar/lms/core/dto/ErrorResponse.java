package com.ghifar.lms.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Error response. Used by all of failed scenarios")
public record ErrorResponse(
        @Schema(description = "The http status", example = "400", requiredMode = Schema.RequiredMode.REQUIRED)
        int status,
        @Schema(description = "The error message", example = "Invalid request", requiredMode = Schema.RequiredMode.REQUIRED)
        String message,
        @Schema(description = "List of error", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<String> errors
) {
}
