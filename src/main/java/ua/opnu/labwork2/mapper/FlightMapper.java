package ua.opnu.labwork2.mapper;

import org.springframework.stereotype.Component;
import ua.opnu.labwork2.dto.flight.FlightCreateRequest;
import ua.opnu.labwork2.dto.flight.FlightResponse;
import ua.opnu.labwork2.dto.flight.FlightUpdateRequest;
import ua.opnu.labwork2.entity.Flight;

@Component
public class FlightMapper {

    public Flight toEntity(FlightCreateRequest request) {
        Flight flight = new Flight();

        flight.setFlightNumber(request.flightNumber());
        flight.setDepartureCity(request.departureCity());
        flight.setArrivalCity(request.arrivalCity());
        flight.setDepartureTime(request.departureTime());

        return flight;
    }

    public FlightResponse toResponse(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureCity(),
                flight.getArrivalCity(),
                flight.getDepartureTime()
        );
    }

    public void updateEntity(Flight flight, FlightUpdateRequest request) {
        flight.setFlightNumber(request.flightNumber());
        flight.setDepartureCity(request.departureCity());
        flight.setArrivalCity(request.arrivalCity());
        flight.setDepartureTime(request.departureTime());
    }
}