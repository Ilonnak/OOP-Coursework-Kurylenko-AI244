package ua.opnu.labwork2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.labwork2.entity.Passenger;
import ua.opnu.labwork2.exception.ConflictOperationException;
import ua.opnu.labwork2.exception.DuplicateResourceException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.repository.PassengerRepository;

import java.util.List;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;

    @Autowired
    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Passenger getPassengerById(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger with id " + id + " not found"));
    }

    public Passenger savePassenger(Passenger passenger) {
        if (passengerRepository.existsByEmail(passenger.getEmail())) {
            throw new DuplicateResourceException("Passenger with email " + passenger.getEmail() + " already exists");
        }

        return passengerRepository.save(passenger);
    }

    public Passenger updatePassenger(Long id, Passenger passenger) {
        Passenger existingPassenger = getPassengerById(id);

        if (passengerRepository.existsByEmailAndIdNot(passenger.getEmail(), id)) {
            throw new DuplicateResourceException("Passenger with email " + passenger.getEmail() + " already exists");
        }

        existingPassenger.setFirstName(passenger.getFirstName());
        existingPassenger.setLastName(passenger.getLastName());
        existingPassenger.setEmail(passenger.getEmail());
        existingPassenger.setPassportNumber(passenger.getPassportNumber());

        return passengerRepository.save(existingPassenger);
    }

    public void deletePassenger(Long id) {
        Passenger passenger = getPassengerById(id);

        if (passenger.getBookings() != null && !passenger.getBookings().isEmpty()) {
            throw new ConflictOperationException("Cannot delete passenger because passenger has related bookings");
        }

        passengerRepository.delete(passenger);
    }
}