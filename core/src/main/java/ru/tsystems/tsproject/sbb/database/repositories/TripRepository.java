package ru.tsystems.tsproject.sbb.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsystems.tsproject.sbb.database.entity.Trip;

import java.util.Date;
import java.util.List;

/**
 * Created by apple on 11.11.14.
 */
public interface TripRepository extends JpaRepository<Trip, Integer> {
    List<Trip> findByRoute_IdAndDepartureTimeBetween(int routeId, Date timeFrom, Date timeTo);
    List<Trip> findByTrain_Id(int trainId);
    List<Trip> findByRoute_Id(int routeId);
}
