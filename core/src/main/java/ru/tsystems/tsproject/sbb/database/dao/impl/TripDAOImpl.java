package ru.tsystems.tsproject.sbb.database.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.tsproject.sbb.database.dao.TripDAO;
import ru.tsystems.tsproject.sbb.database.entity.Trip;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by apple on 16.10.14.
 */

@Repository
public final class TripDAOImpl implements TripDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Trip> getTripsByInterval(int routeId, Date timeFrom, Date timeTo) {
         return entityManager.createQuery("select t from Trip t where t.route.id = :id " +
                                   "and t.departureTime >= :timeFrom and t.departureTime <= :timeTo")
                             .setParameter("id", routeId)
                             .setParameter("timeFrom", timeFrom)
                             .setParameter("timeTo", timeTo)
                             .getResultList();
    }

    public List<Trip> getTripsByRoute(int routeId, Date depDate) {
        return entityManager.createNamedQuery("Trip.getTripsByRoute", Trip.class)
                            .setParameter("id", routeId)
                            .setParameter("date", depDate).getResultList();
    }

    @Override
    public Trip getTrip(int tripId) {
        return (Trip) entityManager.createQuery("select t from Trip t where t.id = :id")
                                   .setParameter("id", tripId).getSingleResult();
    }

    @Override
    public List<Trip> getAllTrips() {
        return entityManager.createQuery("select t from Trip t").getResultList();
    }
}
