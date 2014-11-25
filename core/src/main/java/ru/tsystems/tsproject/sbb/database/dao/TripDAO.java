package ru.tsystems.tsproject.sbb.database.dao;

import ru.tsystems.tsproject.sbb.database.entity.Trip;

import java.util.Date;
import java.util.List;

/**
 * Created by apple on 16.10.14.
 */
public interface TripDAO {
    List<Trip> getTripsByInterval(int route_id, Date timeFrom, Date timeTo);
    List<Trip> getTripsByRoute(int routeId, Date depDate);
    Trip getTrip (int tripId);
    List<Trip> getAllTrips();
}
