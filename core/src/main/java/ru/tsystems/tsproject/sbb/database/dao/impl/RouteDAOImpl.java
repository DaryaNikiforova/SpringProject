package ru.tsystems.tsproject.sbb.database.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.tsproject.sbb.database.dao.RouteDAO;
import ru.tsystems.tsproject.sbb.database.entity.Route;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by apple on 19.10.14.
 */

@Repository
public class RouteDAOImpl implements RouteDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Route> getRoutes() {
        return entityManager.createQuery("select r from Route r").getResultList();
    }

    @Override
    public void addRoute(Route route) {
        entityManager.persist(route);
    }
}
