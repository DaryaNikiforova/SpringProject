package ru.tsystems.tsproject.sbb.database.dao;

import ru.tsystems.tsproject.sbb.database.entity.RouteEntry;

import java.util.List;

/**
 * Created by apple on 14.10.14.
 */
public interface RouteEntryDAO {
    List<RouteEntry> getRouteByStations(String stationFrom, String stationTo);
    RouteEntry getLastRouteEntry(int routeId);
    List<RouteEntry> findRoutesByStation(String station);
    RouteEntry getEntry(String station, int routeId);
    List<RouteEntry> getStations(int routeId);
}
