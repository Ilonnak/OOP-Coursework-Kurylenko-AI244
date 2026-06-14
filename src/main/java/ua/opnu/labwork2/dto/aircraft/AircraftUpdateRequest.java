package ua.opnu.labwork2.dto.aircraft;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO для оновлення інформації про літак")
public record AircraftUpdateRequest(

        @Schema(description = "Модель літака", example = "Airbus A320", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Model is required")
        @Size(min = 2, max = 150, message = "Model must be from 2 to 150 characters")
        String model,

        @Schema(description = "Кількість пасажирських місць у літаку", example = "160", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Capacity is required")
        @Positive(message = "Capacity must be positive")
        Integer capacity,

        @Schema(description = "Виробник літака", example = "Airbus", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Manufacturer is required")
        @Size(min = 2, max = 150, message = "Manufacturer must be from 2 to 150 characters")
        String manufacturer
) {
}