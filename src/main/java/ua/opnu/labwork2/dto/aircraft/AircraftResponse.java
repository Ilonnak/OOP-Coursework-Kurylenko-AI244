package ua.opnu.labwork2.dto.aircraft;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO відповіді з інформацією про літак")
public record AircraftResponse(

        @Schema(description = "Унікальний ідентифікатор літака", example = "1")
        Long id,

        @Schema(description = "Модель літака", example = "Boeing 737")
        String model,

        @Schema(description = "Кількість пасажирських місць", example = "180")
        Integer capacity,

        @Schema(description = "Виробник літака", example = "Boeing")
        String manufacturer
) {
}