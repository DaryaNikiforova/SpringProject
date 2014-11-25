package ru.tsystems.tsproject.sbb.database.dao.impl;

import ru.tsystems.tsproject.sbb.database.dao.*;

import javax.persistence.EntityManager;

/**
 * Represents a factory for DAO entities. Creates entity manager and contains methods
 * for getting instances of DAO instances. The EntityManager object sends into constructor of
 * each entity class.
 * @author Daria Nikiforova
 */
public class FactoryDAOImpl implements FactoryDAO {

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOImpl();
    }

    @Override
    public TrainDAO getTrainDAO() {
        return new TrainDAOImpl();
    }

    @Override
    public TicketDAO getTicketDAO() {
        return new TicketDAOImpl();
    }

    @Override
    public StationDAO getStationDAO() {
        return new StationDAOImpl();
    }

    @Override
    public RouteEntryDAO getRouteEntryDAO() {
        return new RouteEntryDAOImpl();
    }

    @Override
    public TripDAO getTripDAO() {
        return new TripDAOImpl();
    }

    @Override
    public RouteDAO getRouteDAO() {
        return new RouteDAOImpl();
    }

    @Override
    public RateDAO getRateDAO() {
        return new RateDAOImpl();
    }

    @Override
    public ServiceDAO getServiceDAO() {
        return new ServiceDAOImpl();
    }


}
