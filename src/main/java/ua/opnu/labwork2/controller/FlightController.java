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
import ua.opnu.labwork2.dto.error.ApiErrorResponse;
import ua.opnu.labwork2.dto.flight.FlightCreateRequest;
import ua.opnu.labwork2.dto.flight.FlightResponse;
import ua.opnu.labwork2.dto.flight.FlightUpdateRequest;
import ua.opnu.labwork2.dto.passenger.PassengerResponse;
import ua.opnu.labwork2.entity.Booking;
import ua.opnu.labwork2.entity.Flight;
import ua.opnu.labwork2.mapper.FlightMapper;
import ua.opnu.labwork2.mapper.PassengerMapper;
import ua.opnu.labwork2.service.BookingService;
import ua.opnu.labwork2.service.FlightService;

import java.util.List;

@RestController
@RequestMapping("/flights")
@Tag(
        name = "Рейси",
        description = "REST API для створення, перегляду, оновлення та видалення рейсів у системі бронювання авіаквитків."
)
public class FlightController {

    private final FlightService flightService;
    private final BookingService bookingService;
    private final FlightMapper flightMapper;
    private final PassengerMapper passengerMapper;

    @Autowired
    public FlightController(FlightService flightService,
                            BookingService bookingService,
                            FlightMapper flightMapper,
                            PassengerMapper passengerMapper) {
        this.flightService = flightService;
        this.bookingService = bookingService;
        this.flightMapper = flightMapper;
        this.passengerMapper = passengerMapper;
    }

    @Operation(
            summary = "Отримати список рейсів",
            description = "Повертає список усіх рейсів, які збережені в системі бронювання авіаквитків."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список рейсів успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FlightResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<FlightResponse>> getAllFlights() {
        List<FlightResponse> flights = flightService.getAllFlights()
                .stream()
                .map(flightMapper::toResponse)
                .toList();

        return ResponseEntity.ok(flights);
    }

    @Operation(
            summary = "Отримати рейс за id",
            description = "Повертає інформацію про конкретний рейс за його унікальним ідентифікатором. Якщо рейс не знайдено, сервер повертає помилку 404."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рейс успішно знайдено",
                    content = @Content(schema = @Schema(implementation = FlightResponse.class))),
            @ApiResponse(responseCode = "404", description = "Рейс з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable Long id) {
        Flight flight = flightService.getFlightById(id);
        return ResponseEntity.ok(flightMapper.toResponse(flight));
    }

    @Operation(
            summary = "Створити новий рейс",
            description = "Створює новий рейс. Номер рейсу, місто відправлення, місто прибуття та дата відправлення є обов’язковими. Дані перевіряються за правилами валідації DTO."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Рейс успішно створено",
                    content = @Content(schema = @Schema(implementation = FlightResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні вхідні дані або помилка валідації DTO",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Створення рейсу створює конфлікт даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@Valid @RequestBody FlightCreateRequest request) {
        Flight flight = flightMapper.toEntity(request);
        Flight savedFlight = flightService.saveFlight(flight);

        return ResponseEntity.status(HttpStatus.CREATED).body(flightMapper.toResponse(savedFlight));
    }

    @Operation(
            summary = "Оновити рейс",
            description = "Оновлює інформацію про рейс за id. Перевіряється існування рейсу та коректність вхідних даних."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рейс успішно оновлено",
                    content = @Content(schema = @Schema(implementation = FlightResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані для оновлення або помилка валідації DTO",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Рейс з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Оновлення рейсу створює конфлікт даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<FlightResponse> updateFlight(@PathVariable Long id,
                                                       @Valid @RequestBody FlightUpdateRequest request) {
        Flight flight = new Flight();
        flightMapper.updateEntity(flight, request);

        Flight updatedFlight = flightService.updateFlight(id, flight);

        return ResponseEntity.ok(flightMapper.toResponse(updatedFlight));
    }

    @Operation(
            summary = "Видалити рейс",
            description = "Видаляє рейс із системи за його ідентифікатором. Якщо рейс пов’язаний з активними бронюваннями, операція може бути заборонена бізнес-правилами."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Рейс успішно видалено",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Рейс з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Рейс неможливо видалити через пов’язані активні записи",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Отримати рейси літака",
            description = "Повертає список рейсів, які виконуються конкретним літаком. Перед виконанням може перевірятися існування літака."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список рейсів літака успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FlightResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Літак з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/aircraft/{aircraftId}")
    public ResponseEntity<List<FlightResponse>> getFlightsByAircraft(@PathVariable Long aircraftId) {
        List<FlightResponse> flights = flightService.getFlightsByAircraftId(aircraftId)
                .stream()
                .map(flightMapper::toResponse)
                .toList();

        return ResponseEntity.ok(flights);
    }

    @Operation(
            summary = "Отримати пасажирів рейсу",
            description = "Повертає список пасажирів, які мають бронювання на конкретний рейс."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пасажирів рейсу успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PassengerResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Рейс з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}/passengers")
    public ResponseEntity<List<PassengerResponse>> getFlightPassengers(@PathVariable Long id) {
        List<Booking> bookings = bookingService.getAllBookings();

        List<PassengerResponse> passengers = bookings.stream()
                .filter(booking -> booking.getFlights() != null &&
                        booking.getFlights().stream().anyMatch(flight -> flight.getId().equals(id)))
                .map(Booking::getPassenger)
                .filter(passenger -> passenger != null)
                .map(passengerMapper::toResponse)
                .toList();

        return ResponseEntity.ok(passengers);
    }
}