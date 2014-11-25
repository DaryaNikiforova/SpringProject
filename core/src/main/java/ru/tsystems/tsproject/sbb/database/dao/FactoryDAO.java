package ru.tsystems.tsproject.sbb.database.dao;

/**
 * Created by apple on 15.10.14.
 */
public interface FactoryDAO {
    UserDAO getUserDAO();
    TrainDAO getTrainDAO();
    TicketDAO getTicketDAO();
    StationDAO getStationDAO();
    RouteEntryDAO getRouteEntryDAO();
    TripDAO getTripDAO();
    RouteDAO getRouteDAO();
    RateDAO getRateDAO();
    ServiceDAO getServiceDAO();
}

