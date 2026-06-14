package ua.opnu.labwork2.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToMany
    @JoinTable(
            name = "booking_flights",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id")
    )
    private List<Flight> flights;

    public Booking() {
    }

    public Booking(Long id, LocalDateTime bookingDate, BookingStatus status) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public LocalDateTime getBookingDate() { return bookingDate; }

    public BookingStatus getStatus() { return status; }

    public Passenger getPassenger() { return passenger; }

    public void setPassenger(Passenger passenger) { this.passenger = passenger; }

    public List<Flight> getFlights() { return flights; }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}