package ua.opnu.labwork2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.labwork2.entity.SeatClass;
import ua.opnu.labwork2.exception.ConflictOperationException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.repository.SeatClassRepository;

import java.util.List;

@Service
public class SeatClassService {

    private final SeatClassRepository seatClassRepository;

    @Autowired
    public SeatClassService(SeatClassRepository seatClassRepository) {
        this.seatClassRepository = seatClassRepository;
    }

    public List<SeatClass> getAllSeatClasses() {
        return seatClassRepository.findAll();
    }

    public SeatClass getSeatClassById(Long id) {
        return seatClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat class with id " + id + " not found"));
    }

    public SeatClass saveSeatClass(SeatClass seatClass) {
        return seatClassRepository.save(seatClass);
    }

    public SeatClass updateSeatClass(Long id, SeatClass seatClass) {
        SeatClass existingSeatClass = getSeatClassById(id);

        existingSeatClass.setName(seatClass.getName());
        existingSeatClass.setDescription(seatClass.getDescription());

        return seatClassRepository.save(existingSeatClass);
    }

    public void deleteSeatClass(Long id) {
        SeatClass seatClass = getSeatClassById(id);

        if (seatClass.getFlights() != null && !seatClass.getFlights().isEmpty()) {
            throw new ConflictOperationException("Cannot delete seat class because it is related to flights");
        }

        seatClassRepository.delete(seatClass);
    }
}