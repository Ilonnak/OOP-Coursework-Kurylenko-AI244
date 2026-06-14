package ua.opnu.labwork2.dto.seatclass;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO для створення нового класу місць")
public record SeatClassCreateRequest(

        @Schema(description = "Назва класу місць", example = "Business", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 150, message = "Name must be from 2 to 150 characters")
        String name,

        @Schema(description = "Опис класу місць", example = "Комфортний клас місць з додатковим простором для ніг та покращеним сервісом", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 2000, message = "Description must be from 10 to 2000 characters")
        String description
) {
}