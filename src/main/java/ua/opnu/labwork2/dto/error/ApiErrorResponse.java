package ua.opnu.labwork2.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Єдиний формат відповіді сервера у разі виникнення помилки")
public record ApiErrorResponse(

        @Schema(description = "Дата і час виникнення помилки", example = "2026-05-29T19:35:16")
        LocalDateTime timestamp,

        @Schema(description = "HTTP-статус помилки", example = "400")
        int status,

        @Schema(description = "Назва HTTP-помилки", example = "Bad Request")
        String error,

        @Schema(description = "Загальне повідомлення про помилку", example = "Validation failed")
        String message,

        @Schema(description = "URL запиту, під час якого виникла помилка", example = "/passengers")
        String path,

        @Schema(description = "Мапа помилок валідації окремих полів", example = "{\"email\":\"must be a well-formed email address\"}")
        Map<String, String> errors
) {
}