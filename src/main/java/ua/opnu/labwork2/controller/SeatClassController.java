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
import ua.opnu.labwork2.dto.flight.FlightResponse;
import ua.opnu.labwork2.dto.seatclass.SeatClassCreateRequest;
import ua.opnu.labwork2.dto.seatclass.SeatClassResponse;
import ua.opnu.labwork2.dto.seatclass.SeatClassUpdateRequest;
import ua.opnu.labwork2.entity.Flight;
import ua.opnu.labwork2.entity.SeatClass;
import ua.opnu.labwork2.mapper.FlightMapper;
import ua.opnu.labwork2.mapper.SeatClassMapper;
import ua.opnu.labwork2.service.FlightService;
import ua.opnu.labwork2.service.SeatClassService;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(
        name = "Класи місць",
        description = "REST API для створення, перегляду, оновлення та видалення класів місць, а також прив’язки класів місць до рейсів."
)
public class SeatClassController {

    private final SeatClassService seatClassService;
    private final FlightService flightService;
    private final SeatClassMapper seatClassMapper;
    private final FlightMapper flightMapper;

    @Autowired
    public SeatClassController(SeatClassService seatClassService,
                               FlightService flightService,
                               SeatClassMapper seatClassMapper,
                               FlightMapper flightMapper) {
        this.seatClassService = seatClassService;
        this.flightService = flightService;
        this.seatClassMapper = seatClassMapper;
        this.flightMapper = flightMapper;
    }

    @Operation(
            summary = "Створити новий клас місць",
            description = "Створює новий клас місць для рейсів. Назва та опис класу є обов’язковими. Опис повинен містити від 10 до 2000 символів."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Клас місць успішно створено",
                    content = @Content(schema = @Schema(implementation = SeatClassResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні вхідні дані або помилка валідації DTO",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Створення класу місць створює конфлікт даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/seat-classes")
    public ResponseEntity<SeatClassResponse> createSeatClass(@Valid @RequestBody SeatClassCreateRequest request) {
        SeatClass seatClass = seatClassMapper.toEntity(request);
        SeatClass savedSeatClass = seatClassService.saveSeatClass(seatClass);

        return ResponseEntity.status(HttpStatus.CREATED).body(seatClassMapper.toResponse(savedSeatClass));
    }

    @Operation(
            summary = "Отримати список класів місць",
            description = "Повертає список усіх класів місць, які доступні в системі бронювання авіаквитків."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список класів місць успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SeatClassResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/seat-classes")
    public ResponseEntity<List<SeatClassResponse>> getAllSeatClasses() {
        List<SeatClassResponse> seatClasses = seatClassService.getAllSeatClasses()
                .stream()
                .map(seatClassMapper::toResponse)
                .toList();

        return ResponseEntity.ok(seatClasses);
    }

    @Operation(
            summary = "Отримати клас місць за id",
            description = "Повертає інформацію про конкретний клас місць за його унікальним ідентифікатором. Якщо клас місць не знайдено, сервер повертає помилку 404."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клас місць успішно знайдено",
                    content = @Content(schema = @Schema(implementation = SeatClassResponse.class))),
            @ApiResponse(responseCode = "404", description = "Клас місць з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/seat-classes/{id}")
    public ResponseEntity<SeatClassResponse> getSeatClassById(@PathVariable Long id) {
        SeatClass seatClass = seatClassService.getSeatClassById(id);
        return ResponseEntity.ok(seatClassMapper.toResponse(seatClass));
    }

    @Operation(
            summary = "Оновити клас місць",
            description = "Оновлює назву та опис класу місць за його id. Перед оновленням перевіряється існування класу місць та коректність вхідних даних."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клас місць успішно оновлено",
                    content = @Content(schema = @Schema(implementation = SeatClassResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані для оновлення або помилка валідації DTO",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Клас місць з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Оновлення класу місць створює конфлікт даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/seat-classes/{id}")
    public ResponseEntity<SeatClassResponse> updateSeatClass(@PathVariable Long id,
                                                             @Valid @RequestBody SeatClassUpdateRequest request) {
        SeatClass seatClass = new SeatClass();
        seatClassMapper.updateEntity(seatClass, request);

        SeatClass updatedSeatClass = seatClassService.updateSeatClass(id, seatClass);

        return ResponseEntity.ok(seatClassMapper.toResponse(updatedSeatClass));
    }

    @Operation(
            summary = "Отримати рейси за класом місць",
            description = "Повертає список рейсів, у яких доступний конкретний клас місць. Якщо клас місць не існує, сервер повертає помилку 404."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список рейсів успішно отримано",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FlightResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Клас місць з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/seat-classes/{id}/flights")
    public ResponseEntity<List<FlightResponse>> getFlightsBySeatClass(@PathVariable Long id) {
        SeatClass seatClass = seatClassService.getSeatClassById(id);

        List<FlightResponse> flights = seatClass.getFlights()
                .stream()
                .map(flightMapper::toResponse)
                .toList();

        return ResponseEntity.ok(flights);
    }

    @Operation(
            summary = "Видалити клас місць",
            description = "Видаляє клас місць за його id. Якщо клас використовується в активних рейсах, операція може бути заборонена бізнес-правилами."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Клас місць успішно видалено",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Клас місць з указаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Клас місць неможливо видалити через пов’язані рейси",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/seat-classes/{id}")
    public ResponseEntity<Void> deleteSeatClass(@PathVariable Long id) {
        seatClassService.deleteSeatClass(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Додати клас місць до рейсу",
            description = "Додає клас місць до конкретного рейсу. Перед виконанням перевіряється існування рейсу та класу місць. Якщо клас уже доданий до рейсу, повторне додавання не виконується."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клас місць успішно додано до рейсу",
                    content = @Content(schema = @Schema(implementation = FlightResponse.class))),
            @ApiResponse(responseCode = "404", description = "Рейс або клас місць не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Конфлікт даних під час додавання класу місць до рейсу",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/flights/{id}/seat-classes/{classId}")
    public ResponseEntity<FlightResponse> addSeatClassToFlight(@PathVariable Long id,
                                                               @PathVariable Long classId) {
        Flight flight = flightService.getFlightById(id);
        SeatClass seatClass = seatClassService.getSeatClassById(classId);

        List<SeatClass> seatClasses = flight.getSeatClasses();
        if (seatClasses == null) {
            seatClasses = new ArrayList<>();
        }

        boolean exists = seatClasses.stream().anyMatch(sc -> sc.getId().equals(classId));
        if (!exists) {
            seatClasses.add(seatClass);
        }

        flight.setSeatClasses(seatClasses);
        Flight savedFlight = flightService.saveFlight(flight);

        return ResponseEntity.ok(flightMapper.toResponse(savedFlight));
    }

    @Operation(
            summary = "Видалити клас місць з рейсу",
            description = "Видаляє зв’язок між рейсом і класом місць. Перед виконанням перевіряється існування рейсу та класу місць."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клас місць успішно видалено з рейсу",
                    content = @Content(schema = @Schema(implementation = FlightResponse.class))),
            @ApiResponse(responseCode = "404", description = "Рейс або клас місць не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/flights/{id}/seat-classes/{classId}")
    public ResponseEntity<FlightResponse> removeSeatClassFromFlight(@PathVariable Long id,
                                                                    @PathVariable Long classId) {
        Flight flight = flightService.getFlightById(id);
        seatClassService.getSeatClassById(classId);

        List<SeatClass> seatClasses = flight.getSeatClasses();
        if (seatClasses != null) {
            seatClasses.removeIf(sc -> sc.getId().equals(classId));
        }

        flight.setSeatClasses(seatClasses);
        Flight savedFlight = flightService.saveFlight(flight);

        return ResponseEntity.ok(flightMapper.toResponse(savedFlight));
    }
}