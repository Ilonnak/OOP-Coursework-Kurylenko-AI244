package ua.opnu.labwork2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.labwork2.entity.Aircraft;
import ua.opnu.labwork2.entity.Flight;
import ua.opnu.labwork2.exception.BadRequestException;
import ua.opnu.labwork2.exception.ConflictOperationException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.repository.AircraftRepository;
import ua.opnu.labwork2.repository.FlightRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AircraftRepository aircraftRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository, AircraftRepository aircraftRepository) {
        this.flightRepository = flightRepository;
        this.aircraftRepository = aircraftRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight with id " + id + " not found"));
    }

    public Flight saveFlight(Flight flight) {
        validateFlightDate(flight);

        if (flight.getAircraft() != null && flight.getAircraft().getId() != null) {
            Aircraft realAircraft = aircraftRepository.findById(flight.getAircraft().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Aircraft with id " + flight.getAircraft().getId() + " not found"
                    ));

            flight.setAircraft(realAircraft);
        }

        return flightRepository.save(flight);
    }

    public Flight updateFlight(Long id, Flight flight) {
        Flight existingFlight = getFlightById(id);

        validateFlightDate(flight);

        existingFlight.setFlightNumber(flight.getFlightNumber());
        existingFlight.setDepartureCity(flight.getDepartureCity());
        existingFlight.setArrivalCity(flight.getArrivalCity());
        existingFlight.setDepartureTime(flight.getDepartureTime());

        return flightRepository.save(existingFlight);
    }

    public void deleteFlight(Long id) {
        Flight flight = getFlightById(id);

        if (flight.getBookings() != null && !flight.getBookings().isEmpty()) {
            throw new ConflictOperationException("Cannot delete flight because it has related bookings");
        }

        flightRepository.delete(flight);
    }

    public List<Flight> getFlightsByAircraftId(Long aircraftId) {
        if (!aircraftRepository.existsById(aircraftId)) {
            throw new ResourceNotFoundException("Aircraft with id " + aircraftId + " not found");
        }

        return flightRepository.findByAircraftId(aircraftId);
    }

    private void validateFlightDate(Flight flight) {
        if (flight.getDepartureTime() != null && flight.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Departure time cannot be in the past");
        }
    }
}