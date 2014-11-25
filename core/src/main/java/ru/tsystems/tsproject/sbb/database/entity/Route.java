package ru.tsystems.tsproject.sbb.database.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents trains route. One route can uses by different trips and route has one
 * or more route entry.
 * @author Daria Nikiforova
 */
@Entity
public class Route {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<RouteEntry> getRouteEntries() {
        return routeEntries;
    }

    public void setRouteEntries(List<RouteEntry> routeEntries) {
        this.routeEntries = routeEntries;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Route() {
        trips = new ArrayList<Trip>();
        routeEntries = new ArrayList<RouteEntry>();
    }

    @OneToMany(mappedBy = "route")
    List<Trip> trips;

    @OneToMany(mappedBy = "route", cascade = {CascadeType.PERSIST})
    List<RouteEntry> routeEntries;
}
