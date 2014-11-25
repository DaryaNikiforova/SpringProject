package ru.tsystems.tsproject.sbb.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsystems.tsproject.sbb.database.entity.Station;

/**
 * Created by apple on 11.11.14.
 */
public interface StationRepository extends JpaRepository<Station, Integer> {
    Station findByName(String name);
    Long countByName(String name);
}
