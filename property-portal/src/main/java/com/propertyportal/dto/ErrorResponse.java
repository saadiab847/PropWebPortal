package com.propertyportal.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard error response model")
public record ErrorResponse(
    @Schema(description = "Timestamp when the error occurred", example = "2023-05-01T15:30:45.123")
    String timestamp,
    
    @Schema(description = "HTTP status code", example = "400")
    int status,
    
    @Schema(description = "Error type", example = "Bad Request")
    String error,
    
    @Schema(description = "Error message", example = "Title cannot be empty")
    String message,
    
    @Schema(description = "Request path", example = "/api/properties")
    String path
) {
    public ErrorResponse {
        if (timestamp == null) {
            timestamp = LocalDateTime.now().toString();
        }
    }
}