package ua.opnu.labwork2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.opnu.labwork2.dto.aircraft.AircraftCreateRequest;
import ua.opnu.labwork2.dto.aircraft.AircraftResponse;
import ua.opnu.labwork2.dto.aircraft.AircraftUpdateRequest;
import ua.opnu.labwork2.dto.error.ApiErrorResponse;
import ua.opnu.labwork2.dto.flight.FlightResponse;
import ua.opnu.labwork2.entity.Aircraft;
import ua.opnu.labwork2.mapper.AircraftMapper;
import ua.opnu.labwork2.mapper.FlightMapper;
import ua.opnu.labwork2.service.AircraftService;
import ua.opnu.labwork2.service.FlightService;

import java.util.List;

@RestController
@RequestMapping("/aircraft")
@Tag(
        name = "Літаки",
        description = "REST API для створення, перегляду, оновлення та видалення літаків, а також отримання рейсів конкретного літака."
)
public class AircraftController {

    private final AircraftService aircraftService;
    private final FlightService flightService;
    private final AircraftMapper aircraftMapper;
    private final FlightMapper flightMapper;

    @Autowired
    public AircraftController(AircraftService aircraftService,
                              FlightService flightService,
                              AircraftMapper aircraftMapper,
                              FlightMapper flightMapper) {
        this.aircraftService = aircraftService;
        this.flightService = flightService;
        this.aircraftMapper = aircraftMapper;
        this.flightMapper = flightMapper;
    }

    @Operation(
            summary = "Отримати список літаків",
            description = "Повертає список усіх літаків, які зареєстровані в системі бронювання авіаквитків."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список літаків успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AircraftResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<AircraftResponse>> getAllAircraft() {
        List<AircraftResponse> aircraft = aircraftService.getAllAircraft()
                .stream()
                .map(aircraftMapper::toResponse)
                .toList();

        return ResponseEntity.ok(aircraft);
    }

    @Operation(
            summary = "Отримати літак за id",
            description = "Повертає інформацію про конкретний літак за його унікальним ідентифікатором. Якщо літак не знайдено, сервер повертає помилку 404."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Літак успішно знайдено",
                    content = @Content(schema = @Schema(implementation = AircraftResponse.class))),
            @ApiResponse(responseCode = "404", description = "Літак з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AircraftResponse> getAircraftById(@PathVariable Long id) {
        Aircraft aircraft = aircraftService.getAircraftById(id);
        return ResponseEntity.ok(aircraftMapper.toResponse(aircraft));
    }

    @Operation(
            summary = "Створити новий літак",
            description = "Створює новий літак. Модель, місткість та виробник є обов’язковими. Місткість літака повинна бути додатним числом."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Літак успішно створено",
                    content = @Content(schema = @Schema(implementation = AircraftResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні вхідні дані або помилка валідації DTO",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Створення літака створює конфлікт даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<AircraftResponse> createAircraft(@Valid @RequestBody AircraftCreateRequest request) {
        Aircraft aircraft = aircraftMapper.toEntity(request);
        Aircraft savedAircraft = aircraftService.saveAircraft(aircraft);

        return ResponseEntity.status(HttpStatus.CREATED).body(aircraftMapper.toResponse(savedAircraft));
    }

    @Operation(
            summary = "Оновити літак",
            description = "Оновлює дані літака за id. Дозволено змінювати модель, місткість та виробника. Перед оновленням перевіряється існування літака."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Дані літака успішно оновлено",
                    content = @Content(schema = @Schema(implementation = AircraftResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані для оновлення або помилка валідації DTO",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Літак з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Оновлення літака створює конфлікт даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<AircraftResponse> updateAircraft(@PathVariable Long id,
                                                           @Valid @RequestBody AircraftUpdateRequest request) {
        Aircraft aircraft = new Aircraft();
        aircraftMapper.updateEntity(aircraft, request);

        Aircraft updatedAircraft = aircraftService.updateAircraft(id, aircraft);

        return ResponseEntity.ok(aircraftMapper.toResponse(updatedAircraft));
    }

    @Operation(
            summary = "Видалити літак",
            description = "Видаляє літак за його ідентифікатором. Якщо літак має пов’язані активні рейси, операція може бути заборонена бізнес-правилами."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Літак успішно видалено",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Літак з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Літак неможливо видалити через пов’язані рейси",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraft(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Отримати рейси літака",
            description = "Повертає список рейсів, які виконуються конкретним літаком. Якщо літак не існує, сервер може повернути помилку 404."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список рейсів літака успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FlightResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Літак з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}/flights")
    public ResponseEntity<List<FlightResponse>> getAircraftFlights(@PathVariable Long id) {
        List<FlightResponse> flights = flightService.getFlightsByAircraftId(id)
                .stream()
                .map(flightMapper::toResponse)
                .toList();

        return ResponseEntity.ok(flights);
    }
}