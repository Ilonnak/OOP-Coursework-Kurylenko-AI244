package ua.opnu.labwork2.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.opnu.labwork2.entity.BookingStatus;

import java.time.LocalDateTime;

@Schema(description = "DTO відповіді з інформацією про бронювання")
public record BookingResponse(

        @Schema(description = "Унікальний ідентифікатор бронювання", example = "1")
        Long id,

        @Schema(description = "Дата та час бронювання", example = "2026-06-01T10:15:00")
        LocalDateTime bookingDate,

        @Schema(description = "Поточний статус бронювання", example = "ACTIVE")
        BookingStatus status
) {
}