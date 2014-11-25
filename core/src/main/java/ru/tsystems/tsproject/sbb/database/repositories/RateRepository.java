package ru.tsystems.tsproject.sbb.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsystems.tsproject.sbb.database.entity.Rate;

import java.util.List;

/**
 * Created by apple on 11.11.14.
 */
public interface RateRepository extends JpaRepository<Rate, Integer>{
    List<Rate> findByForTrainTrue();
    List<Rate> findByForTrainFalse();
}
