package ua.opnu.labwork2.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ua.opnu.labwork2.entity.BookingStatus;

import java.time.LocalDateTime;

@Schema(description = "DTO для створення нового бронювання")
public record BookingCreateRequest(

        @Schema(
                description = "Дата та час створення бронювання",
                example = "2026-06-01T10:15:00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Booking date is required")
        LocalDateTime bookingDate,

        @Schema(
                description = "Статус бронювання",
                example = "ACTIVE",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Status is required")
        BookingStatus status
) {
}