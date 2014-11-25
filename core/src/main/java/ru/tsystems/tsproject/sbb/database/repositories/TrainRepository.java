package ru.tsystems.tsproject.sbb.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsystems.tsproject.sbb.database.entity.Train;

/**
 * Created by apple on 11.11.14.
 */
public interface TrainRepository extends JpaRepository<Train, Integer> {
    Long countById(int id);
}
