package ua.opnu.labwork2.dto.seatclass;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO відповіді з інформацією про клас місць")
public record SeatClassResponse(

        @Schema(description = "Унікальний ідентифікатор класу місць", example = "1")
        Long id,

        @Schema(description = "Назва класу місць", example = "Business")
        String name,

        @Schema(description = "Опис класу місць", example = "Комфортний клас місць з додатковим простором для ніг та покращеним сервісом")
        String description
) {
}