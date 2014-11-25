package ru.tsystems.tsproject.sbb.database.dao;

import ru.tsystems.tsproject.sbb.database.entity.Route;

import java.util.List;

/**
 * Created by apple on 19.10.14.
 */
public interface RouteDAO {
    List<Route> getRoutes();
    void addRoute(Route route);
}
