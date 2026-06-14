package ua.opnu.labwork2.dto.flight;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO відповіді з інформацією про рейс")
public record FlightResponse(

        @Schema(
                description = "Унікальний ідентифікатор рейсу",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Номер рейсу",
                example = "PS125"
        )
        String flightNumber,

        @Schema(
                description = "Місто відправлення",
                example = "Одеса"
        )
        String departureCity,

        @Schema(
                description = "Місто прибуття",
                example = "Київ"
        )
        String arrivalCity,

        @Schema(
                description = "Дата та час відправлення",
                example = "2026-06-15T14:30:00"
        )
        LocalDateTime departureTime
) {
}