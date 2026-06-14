package ua.opnu.labwork2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.labwork2.entity.Aircraft;
import ua.opnu.labwork2.exception.ConflictOperationException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.repository.AircraftRepository;

import java.util.List;

@Service
public class AircraftService {

    private final AircraftRepository aircraftRepository;

    @Autowired
    public AircraftService(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    public List<Aircraft> getAllAircraft() {
        return aircraftRepository.findAll();
    }

    public Aircraft getAircraftById(Long id) {
        return aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft with id " + id + " not found"));
    }

    public Aircraft saveAircraft(Aircraft aircraft) {
        return aircraftRepository.save(aircraft);
    }

    public Aircraft updateAircraft(Long id, Aircraft aircraft) {
        Aircraft existingAircraft = getAircraftById(id);

        existingAircraft.setModel(aircraft.getModel());
        existingAircraft.setCapacity(aircraft.getCapacity());
        existingAircraft.setManufacturer(aircraft.getManufacturer());

        return aircraftRepository.save(existingAircraft);
    }

    public void deleteAircraft(Long id) {
        Aircraft aircraft = getAircraftById(id);

        if (aircraft.getFlights() != null && !aircraft.getFlights().isEmpty()) {
            throw new ConflictOperationException("Cannot delete aircraft because it has related flights");
        }

        aircraftRepository.delete(aircraft);
    }
}