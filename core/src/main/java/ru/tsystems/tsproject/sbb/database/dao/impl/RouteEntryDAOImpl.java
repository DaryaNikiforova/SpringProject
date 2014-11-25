package ru.tsystems.tsproject.sbb.database.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.tsproject.sbb.database.dao.RouteEntryDAO;
import ru.tsystems.tsproject.sbb.database.entity.RouteEntry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by apple on 16.10.14.
 */

@Repository

public final class RouteEntryDAOImpl implements RouteEntryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RouteEntry> getRouteByStations(String stationFrom, String stationTo) {
        return entityManager.createQuery("select r1 from Route route " +
                "join route.routeEntries r1 " +
                "join route.routeEntries r2 " +
                "where r1.station.name = :stationA and r2.station.name = :stationB " +
                "and r1.route.id = r2.route.id and r1.seqNumber < r2.seqNumber")
                     .setParameter("stationA", stationFrom)
                     .setParameter("stationB", stationTo).getResultList();
    }

    @Override
    public RouteEntry getLastRouteEntry(int routeId) {
        int seqNumber = (Integer) entityManager.createQuery("select max(r.seqNumber) from RouteEntry r where r.route.id = :id")
                                               .setParameter("id", routeId).getSingleResult();
        return (RouteEntry) entityManager.createQuery("select r from RouteEntry r where r.route.id = :routeId and r.seqNumber = :maxNumber")
                                         .setParameter("routeId", routeId)
                                         .setParameter("maxNumber", seqNumber).getSingleResult();
    }

    @Override
    public List<RouteEntry> findRoutesByStation(String station) {
        return entityManager.createQuery("select r from RouteEntry r where r.station.name = :name")
                            .setParameter("name", station).getResultList();

    }

    @Override
    public RouteEntry getEntry(String station, int routeId) {
        return (RouteEntry) entityManager.createQuery("select r from RouteEntry r where r.station.name = :station and r.route.id = :id")
                                   .setParameter("station", station).setParameter("id", routeId).getSingleResult();
    }

    @Override
    public List<RouteEntry> getStations(int routeId) {
        return entityManager.createQuery("select r from RouteEntry r where r.route.id = :id")
                            .setParameter("id", routeId).getResultList();
    }
}
