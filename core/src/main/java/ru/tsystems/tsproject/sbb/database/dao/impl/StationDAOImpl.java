package ru.tsystems.tsproject.sbb.database.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.tsproject.sbb.database.entity.Station;
import ru.tsystems.tsproject.sbb.database.dao.StationDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by apple on 04.10.14.
 */

@Repository
public final class StationDAOImpl implements StationDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addStation(Station station) {
        entityManager.persist(station);
    }

    @Override
    public Station getStation(String name) {
        return (Station) entityManager.createQuery("select s from Station s where s.name = :name")
                            .setParameter("name", name).getSingleResult();
    }

    @Override
    public boolean isStationExist(String name) {
        return !entityManager.createQuery("select s from Station s where s.name = :name")
                .setParameter("name", name)
                .getResultList()
                .isEmpty();
    }

    @Override
    public List<String> getStations() {
        return entityManager.createQuery("select s.name from Station s").getResultList();
    }

}
