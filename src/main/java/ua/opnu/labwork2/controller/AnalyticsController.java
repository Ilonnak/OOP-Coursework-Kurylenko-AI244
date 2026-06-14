package ua.opnu.labwork2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.opnu.labwork2.dto.error.ApiErrorResponse;

@Tag(
        name = "Аналітика",
        description = "REST API для отримання статистики та аналітичних даних системи бронювання авіаквитків"
)
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Operation(
            summary = "Отримати кількість рейсів",
            description = "Повертає загальну кількість рейсів, зареєстрованих у системі бронювання авіаквитків"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Кількість рейсів успішно отримано"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/flights/count")
    public ResponseEntity<String> getFlightsCount() {
        return ResponseEntity.ok("Flights count OK");
    }

    @Operation(
            summary = "Отримати кількість пасажирів",
            description = "Повертає загальну кількість пасажирів, зареєстрованих у системі"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Кількість пасажирів успішно отримано"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/passengers/count")
    public ResponseEntity<String> getPassengersCount() {
        return ResponseEntity.ok("Passengers count OK");
    }

    @Operation(
            summary = "Отримати активні бронювання",
            description = "Повертає інформацію про активні бронювання у системі"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Активні бронювання успішно отримано"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/bookings/active")
    public ResponseEntity<String> getActiveBookings() {
        return ResponseEntity.ok("Active bookings OK");
    }

    @Operation(
            summary = "Отримати популярні рейси",
            description = "Повертає список найбільш популярних рейсів на основі кількості бронювань"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Популярні рейси успішно отримано"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/flights/popular")
    public ResponseEntity<String> getPopularFlights() {
        return ResponseEntity.ok("Popular flights OK");
    }

    @Operation(
            summary = "Отримати завантаження літаків",
            description = "Повертає аналітичні дані щодо завантаження літаків рейсами"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Дані про завантаження літаків успішно отримано"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/aircraft/workload")
    public ResponseEntity<String> getAircraftWorkload() {
        return ResponseEntity.ok("Aircraft workload OK");
    }
}