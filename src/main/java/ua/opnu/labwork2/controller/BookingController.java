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
import ua.opnu.labwork2.dto.booking.BookingCreateRequest;
import ua.opnu.labwork2.dto.booking.BookingResponse;
import ua.opnu.labwork2.dto.booking.BookingUpdateRequest;
import ua.opnu.labwork2.dto.error.ApiErrorResponse;
import ua.opnu.labwork2.dto.flight.FlightResponse;
import ua.opnu.labwork2.entity.Booking;
import ua.opnu.labwork2.mapper.BookingMapper;
import ua.opnu.labwork2.mapper.FlightMapper;
import ua.opnu.labwork2.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(
        name = "Бронювання",
        description = "REST API для створення, перегляду, оновлення та скасування бронювань авіаквитків."
)
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final FlightMapper flightMapper;

    @Autowired
    public BookingController(BookingService bookingService,
                             BookingMapper bookingMapper,
                             FlightMapper flightMapper) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
        this.flightMapper = flightMapper;
    }

    @Operation(
            summary = "Отримати список бронювань",
            description = "Повертає список усіх бронювань, які зберігаються в системі бронювання авіаквитків."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список бронювань успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookingResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings()
                .stream()
                .map(bookingMapper::toResponse)
                .toList();

        return ResponseEntity.ok(bookings);
    }

    @Operation(
            summary = "Отримати бронювання за id",
            description = "Повертає інформацію про бронювання за його унікальним ідентифікатором. Якщо бронювання не знайдено, сервер повертає помилку 404."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронювання успішно знайдено",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "404", description = "Бронювання з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingMapper.toResponse(booking));
    }

    @Operation(
            summary = "Створити нове бронювання",
            description = "Створює нове бронювання авіаквитків. Дата бронювання та статус є обов’язковими. Статус повинен належати до допустимого набору значень."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Бронювання успішно створено",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні вхідні дані або помилка валідації DTO",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Створення бронювання створює конфлікт даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingCreateRequest request) {
        Booking booking = bookingMapper.toEntity(request);
        Booking savedBooking = bookingService.saveBooking(booking);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookingMapper.toResponse(savedBooking));
    }

    @Operation(
            summary = "Оновити бронювання",
            description = "Оновлює дату та статус бронювання за його id. Перед оновленням перевіряється існування бронювання та коректність вхідних даних."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронювання успішно оновлено",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані для оновлення або помилка валідації DTO",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Бронювання з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Оновлення бронювання створює конфлікт даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id,
                                                         @Valid @RequestBody BookingUpdateRequest request) {
        Booking booking = new Booking();
        bookingMapper.updateEntity(booking, request);

        Booking updatedBooking = bookingService.updateBooking(id, booking);

        return ResponseEntity.ok(bookingMapper.toResponse(updatedBooking));
    }

    @Operation(
            summary = "Скасувати бронювання",
            description = "Видаляє або скасовує бронювання за його ідентифікатором. Якщо операція порушує бізнес-правила, сервер повертає помилку 409."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Бронювання успішно скасовано",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Бронювання з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Бронювання неможливо скасувати через конфлікт бізнес-правил",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Отримати бронювання пасажира",
            description = "Повертає список бронювань, які належать конкретному пасажиру. Якщо пасажир не існує, сервер може повернути помилку 404."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список бронювань пасажира успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookingResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Пасажира з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByPassenger(@PathVariable Long passengerId) {
        List<BookingResponse> bookings = bookingService.getBookingsByPassengerId(passengerId)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();

        return ResponseEntity.ok(bookings);
    }

    @Operation(
            summary = "Отримати рейси бронювання",
            description = "Повертає список рейсів, які входять до конкретного бронювання. Якщо бронювання не знайдено, сервер повертає помилку 404."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список рейсів бронювання успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FlightResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Бронювання з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}/flights")
    public ResponseEntity<List<FlightResponse>> getBookingFlights(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);

        List<FlightResponse> flights = booking.getFlights()
                .stream()
                .map(flightMapper::toResponse)
                .toList();

        return ResponseEntity.ok(flights);
    }
}