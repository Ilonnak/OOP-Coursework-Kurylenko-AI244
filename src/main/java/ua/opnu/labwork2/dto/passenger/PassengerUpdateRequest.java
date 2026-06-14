package ua.opnu.labwork2.dto.passenger;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO для оновлення інформації про пасажира")
public record PassengerUpdateRequest(

        @Schema(
                description = "Ім'я пасажира",
                example = "Ілона",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be from 2 to 50 characters")
        @Pattern(regexp = ".*\\S.*", message = "First name cannot contain only spaces")
        String firstName,

        @Schema(
                description = "Прізвище пасажира",
                example = "Куриленко",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be from 2 to 50 characters")
        @Pattern(regexp = ".*\\S.*", message = "Last name cannot contain only spaces")
        String lastName,

        @Schema(
                description = "Електронна пошта пасажира",
                example = "ilona@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must be up to 100 characters")
        String email,

        @Schema(
                description = "Номер паспорта пасажира",
                example = "ER123456",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Passport number is required")
        @Size(min = 2, max = 30, message = "Passport number must be from 2 to 30 characters")
        @Pattern(regexp = ".*\\S.*", message = "Passport number cannot contain only spaces")
        String passportNumber
) {
}