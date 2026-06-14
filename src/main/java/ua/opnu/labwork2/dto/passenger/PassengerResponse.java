package ua.opnu.labwork2.dto.passenger;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO відповіді з інформацією про пасажира")
public record PassengerResponse(

        @Schema(
                description = "Унікальний ідентифікатор пасажира",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Ім'я пасажира",
                example = "Ілона"
        )
        String firstName,

        @Schema(
                description = "Прізвище пасажира",
                example = "Куриленко"
        )
        String lastName,

        @Schema(
                description = "Електронна пошта пасажира",
                example = "ilona@example.com"
        )
        String email,

        @Schema(
                description = "Номер паспорта пасажира",
                example = "ER123456"
        )
        String passportNumber
) {
}