package ua.opnu.labwork2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.labwork2.entity.Booking;
import ua.opnu.labwork2.entity.BookingStatus;
import ua.opnu.labwork2.entity.Flight;
import ua.opnu.labwork2.entity.Passenger;
import ua.opnu.labwork2.exception.BadRequestException;
import ua.opnu.labwork2.exception.ConflictOperationException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.repository.BookingRepository;
import ua.opnu.labwork2.repository.FlightRepository;
import ua.opnu.labwork2.repository.PassengerRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          PassengerRepository passengerRepository,
                          FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.flightRepository = flightRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + id + " not found"));
    }

    public Booking saveBooking(Booking booking) {
        validateBooking(booking);

        if (booking.getPassenger() != null && booking.getPassenger().getId() != null) {
            Passenger passenger = passengerRepository.findById(booking.getPassenger().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Passenger with id " + booking.getPassenger().getId() + " not found"
                    ));

            booking.setPassenger(passenger);
        }

        if (booking.getFlights() != null) {
            List<Flight> realFlights = booking.getFlights()
                    .stream()
                    .map(flight -> flightRepository.findById(flight.getId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Flight with id " + flight.getId() + " not found"
                            )))
                    .toList();

            booking.setFlights(realFlights);
        }

        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking booking) {
        Booking existingBooking = getBookingById(id);

        validateBooking(booking);

        existingBooking.setBookingDate(booking.getBookingDate());
        existingBooking.setStatus(booking.getStatus());

        return bookingRepository.save(existingBooking);
    }

    public void deleteBooking(Long id) {
        Booking booking = getBookingById(id);

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new ConflictOperationException("Confirmed booking cannot be deleted directly");
        }

        bookingRepository.delete(booking);
    }

    public List<Booking> getBookingsByPassengerId(Long passengerId) {
        if (!passengerRepository.existsById(passengerId)) {
            throw new ResourceNotFoundException("Passenger with id " + passengerId + " not found");
        }

        return bookingRepository.findByPassengerId(passengerId);
    }

    private void validateBooking(Booking booking) {
        if (booking.getBookingDate() != null && booking.getBookingDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Booking date cannot be in the past");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("New or updated booking cannot have CANCELLED status");
        }
    }
}