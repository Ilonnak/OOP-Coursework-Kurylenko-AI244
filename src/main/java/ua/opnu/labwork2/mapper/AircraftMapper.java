package ua.opnu.labwork2.mapper;

import org.springframework.stereotype.Component;
import ua.opnu.labwork2.dto.aircraft.AircraftCreateRequest;
import ua.opnu.labwork2.dto.aircraft.AircraftResponse;
import ua.opnu.labwork2.dto.aircraft.AircraftUpdateRequest;
import ua.opnu.labwork2.entity.Aircraft;

@Component
public class AircraftMapper {

    public Aircraft toEntity(AircraftCreateRequest request) {
        Aircraft aircraft = new Aircraft();

        aircraft.setModel(request.model());
        aircraft.setCapacity(request.capacity());
        aircraft.setManufacturer(request.manufacturer());

        return aircraft;
    }

    public AircraftResponse toResponse(Aircraft aircraft) {
        return new AircraftResponse(
                aircraft.getId(),
                aircraft.getModel(),
                aircraft.getCapacity(),
                aircraft.getManufacturer()
        );
    }

    public void updateEntity(Aircraft aircraft, AircraftUpdateRequest request) {
        aircraft.setModel(request.model());
        aircraft.setCapacity(request.capacity());
        aircraft.setManufacturer(request.manufacturer());
    }
}