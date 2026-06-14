package ua.opnu.labwork2.mapper;

import org.springframework.stereotype.Component;
import ua.opnu.labwork2.dto.passenger.PassengerCreateRequest;
import ua.opnu.labwork2.dto.passenger.PassengerResponse;
import ua.opnu.labwork2.dto.passenger.PassengerUpdateRequest;
import ua.opnu.labwork2.entity.Passenger;

@Component
public class PassengerMapper {

    public Passenger toEntity(PassengerCreateRequest request) {
        Passenger passenger = new Passenger();

        passenger.setFirstName(request.firstName());
        passenger.setLastName(request.lastName());
        passenger.setEmail(request.email());
        passenger.setPassportNumber(request.passportNumber());

        return passenger;
    }

    public PassengerResponse toResponse(Passenger passenger) {
        return new PassengerResponse(
                passenger.getId(),
                passenger.getFirstName(),
                passenger.getLastName(),
                passenger.getEmail(),
                passenger.getPassportNumber()
        );
    }

    public void updateEntity(Passenger passenger, PassengerUpdateRequest request) {
        passenger.setFirstName(request.firstName());
        passenger.setLastName(request.lastName());
        passenger.setEmail(request.email());
        passenger.setPassportNumber(request.passportNumber());
    }
}