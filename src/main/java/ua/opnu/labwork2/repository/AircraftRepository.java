package ua.opnu.labwork2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.labwork2.entity.Aircraft;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}