package ua.opnu.labwork2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.labwork2.entity.SeatClass;

public interface SeatClassRepository extends JpaRepository<SeatClass, Long> {
}