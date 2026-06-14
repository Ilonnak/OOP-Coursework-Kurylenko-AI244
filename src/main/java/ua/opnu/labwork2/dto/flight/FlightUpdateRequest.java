package ua.opnu.labwork2.dto.flight;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "DTO для оновлення інформації про рейс")
public record FlightUpdateRequest(

        @Schema(
                description = "Унікальний номер рейсу",
                example = "PS125",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Flight number is required")
        @Size(min = 2, max = 30, message = "Flight number must be from 2 to 30 characters")
        String flightNumber,

        @Schema(
                description = "Місто відправлення рейсу",
                example = "Одеса",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Departure city is required")
        @Size(min = 2, max = 150, message = "Departure city must be from 2 to 150 characters")
        String departureCity,

        @Schema(
                description = "Місто прибуття рейсу",
                example = "Київ",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Arrival city is required")
        @Size(min = 2, max = 150, message = "Arrival city must be from 2 to 150 characters")
        String arrivalCity,

        @Schema(
                description = "Дата та час відправлення рейсу",
                example = "2026-06-15T14:30:00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Departure time is required")
        LocalDateTime departureTime
) {
}