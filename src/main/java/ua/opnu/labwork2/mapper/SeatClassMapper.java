package ua.opnu.labwork2.mapper;

import org.springframework.stereotype.Component;
import ua.opnu.labwork2.dto.seatclass.SeatClassCreateRequest;
import ua.opnu.labwork2.dto.seatclass.SeatClassResponse;
import ua.opnu.labwork2.dto.seatclass.SeatClassUpdateRequest;
import ua.opnu.labwork2.entity.SeatClass;

@Component
public class SeatClassMapper {

    public SeatClass toEntity(SeatClassCreateRequest request) {
        SeatClass seatClass = new SeatClass();

        seatClass.setName(request.name());
        seatClass.setDescription(request.description());

        return seatClass;
    }

    public SeatClassResponse toResponse(SeatClass seatClass) {
        return new SeatClassResponse(
                seatClass.getId(),
                seatClass.getName(),
                seatClass.getDescription()
        );
    }

    public void updateEntity(SeatClass seatClass, SeatClassUpdateRequest request) {
        seatClass.setName(request.name());
        seatClass.setDescription(request.description());
    }
}