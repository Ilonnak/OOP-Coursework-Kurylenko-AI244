package ua.opnu.labwork2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.opnu.labwork2.dto.error.ApiErrorResponse;

@Tag(
        name = "Пошук",
        description = "REST API для пошуку рейсів, пасажирів та бронювань у системі бронювання авіаквитків"
)
@RestController
@RequestMapping("/search")
public class SearchController {

    @Operation(
            summary = "Пошук рейсів",
            description = "Виконує пошук рейсів за ключовим словом. Пошук може здійснюватися за номером рейсу, містом відправлення або прибуття"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Результати пошуку рейсів успішно отримано"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некоректний пошуковий запит",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/flights")
    public ResponseEntity<String> searchFlights(
            @Parameter(
                    description = "Пошуковий запит для рейсів",
                    example = "Kyiv"
            )
            @RequestParam String query
    ) {
        return ResponseEntity.ok("Search flights: " + query);
    }

    @Operation(
            summary = "Пошук пасажирів",
            description = "Виконує пошук пасажирів за ім’ям, прізвищем, email або номером паспорта"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Результати пошуку пасажирів успішно отримано"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некоректний пошуковий запит",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/passengers")
    public ResponseEntity<String> searchPassengers(
            @Parameter(
                    description = "Пошуковий запит для пасажирів",
                    example = "Куриленко"
            )
            @RequestParam String query
    ) {
        return ResponseEntity.ok("Search passengers: " + query);
    }

    @Operation(
            summary = "Пошук бронювань",
            description = "Виконує пошук бронювань за ідентифікатором, статусом або іншими параметрами бронювання"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Результати пошуку бронювань успішно отримано"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некоректний пошуковий запит",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/bookings")
    public ResponseEntity<String> searchBookings(
            @Parameter(
                    description = "Пошуковий запит для бронювань",
                    example = "ACTIVE"
            )
            @RequestParam String query
    ) {
        return ResponseEntity.ok("Search bookings: " + query);
    }
}