package ua.opnu.labwork2.mapper;

import org.springframework.stereotype.Component;
import ua.opnu.labwork2.dto.booking.BookingCreateRequest;
import ua.opnu.labwork2.dto.booking.BookingResponse;
import ua.opnu.labwork2.dto.booking.BookingUpdateRequest;
import ua.opnu.labwork2.entity.Booking;

@Component
public class BookingMapper {

    public Booking toEntity(BookingCreateRequest request) {
        Booking booking = new Booking();

        booking.setBookingDate(request.bookingDate());
        booking.setStatus(request.status());

        return booking;
    }

    public BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getBookingDate(),
                booking.getStatus()
        );
    }

    public void updateEntity(Booking booking, BookingUpdateRequest request) {
        booking.setBookingDate(request.bookingDate());
        booking.setStatus(request.status());
    }
}