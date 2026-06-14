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
import ua.opnu.labwork2.dto.booking.BookingResponse;
import ua.opnu.labwork2.dto.error.ApiErrorResponse;
import ua.opnu.labwork2.dto.passenger.PassengerCreateRequest;
import ua.opnu.labwork2.dto.passenger.PassengerResponse;
import ua.opnu.labwork2.dto.passenger.PassengerUpdateRequest;
import ua.opnu.labwork2.entity.Passenger;
import ua.opnu.labwork2.mapper.BookingMapper;
import ua.opnu.labwork2.mapper.PassengerMapper;
import ua.opnu.labwork2.service.BookingService;
import ua.opnu.labwork2.service.PassengerService;

import java.util.List;

@Tag(
        name = "Пасажири",
        description = "Операції для керування пасажирами системи бронювання авіаквитків: створення, перегляд, оновлення, видалення та отримання бронювань пасажира."
)
@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;
    private final BookingService bookingService;
    private final PassengerMapper passengerMapper;
    private final BookingMapper bookingMapper;

    @Autowired
    public PassengerController(PassengerService passengerService,
                               BookingService bookingService,
                               PassengerMapper passengerMapper,
                               BookingMapper bookingMapper) {
        this.passengerService = passengerService;
        this.bookingService = bookingService;
        this.passengerMapper = passengerMapper;
        this.bookingMapper = bookingMapper;
    }

    @Operation(
            summary = "Отримати список пасажирів",
            description = "Повертає список усіх пасажирів, зареєстрованих у системі бронювання авіаквитків."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пасажирів успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PassengerResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<PassengerResponse>> getAllPassengers() {
        List<PassengerResponse> passengers = passengerService.getAllPassengers()
                .stream()
                .map(passengerMapper::toResponse)
                .toList();

        return ResponseEntity.ok(passengers);
    }

    @Operation(
            summary = "Отримати пасажира за id",
            description = "Повертає детальну інформацію про пасажира за його унікальним ідентифікатором. Якщо пасажира не знайдено, сервер повертає помилку 404."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пасажира успішно знайдено",
                    content = @Content(schema = @Schema(implementation = PassengerResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пасажира з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        Passenger passenger = passengerService.getPassengerById(id);
        return ResponseEntity.ok(passengerMapper.toResponse(passenger));
    }

    @Operation(
            summary = "Створити нового пасажира",
            description = "Створює нового пасажира. Поля firstName, lastName, email та passportNumber є обов’язковими. Email має відповідати формату електронної пошти та бути унікальним у системі."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пасажира успішно створено",
                    content = @Content(schema = @Schema(implementation = PassengerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні вхідні дані або помилка валідації DTO",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Пасажир з таким email уже існує",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@Valid @RequestBody PassengerCreateRequest request) {
        Passenger passenger = passengerMapper.toEntity(request);
        Passenger savedPassenger = passengerService.savePassenger(passenger);

        return ResponseEntity.status(HttpStatus.CREATED).body(passengerMapper.toResponse(savedPassenger));
    }

    @Operation(
            summary = "Оновити дані пасажира",
            description = "Оновлює дані пасажира за id. Перевіряється існування пасажира, коректність вхідних даних та унікальність email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Дані пасажира успішно оновлено",
                    content = @Content(schema = @Schema(implementation = PassengerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні вхідні дані або помилка валідації DTO",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пасажира з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Оновлення створює конфлікт даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> updatePassenger(@PathVariable Long id,
                                                             @Valid @RequestBody PassengerUpdateRequest request) {
        Passenger passenger = new Passenger();
        passengerMapper.updateEntity(passenger, request);

        Passenger updatedPassenger = passengerService.updatePassenger(id, passenger);

        return ResponseEntity.ok(passengerMapper.toResponse(updatedPassenger));
    }

    @Operation(
            summary = "Видалити пасажира",
            description = "Видаляє пасажира за id. Якщо з пасажиром пов’язані активні бронювання, операція може бути заборонена бізнес-правилами."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пасажира успішно видалено",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пасажира з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Пасажира неможливо видалити через пов’язані активні записи",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Отримати бронювання пасажира",
            description = "Повертає список бронювань, які належать конкретному пасажиру. Перед виконанням перевіряється існування пасажира."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список бронювань пасажира успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookingResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Пасажира з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingResponse>> getPassengerBookings(@PathVariable Long id) {
        List<BookingResponse> bookings = bookingService.getBookingsByPassengerId(id)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();

        return ResponseEntity.ok(bookings);
    }
}