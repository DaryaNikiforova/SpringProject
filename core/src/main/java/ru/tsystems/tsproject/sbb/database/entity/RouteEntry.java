package ru.tsystems.tsproject.sbb.database.entity;

import javax.persistence.*;

/**
 * Represents route unit. Contains information about stations and their sequence
 * for the route. Include one or more records for each station in different routes.
 * Uniqueness defines by sets - "routes id, stations id" and "routes id and sequence number.
 * @author Daria Nikiforova
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"route_id", "station_id"}),
                            @UniqueConstraint(columnNames = {"route_id", "seqNumber"})})
public class RouteEntry {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private double distance;

    private int seqNumber;

    private int hour;

    private int minute;

    @ManyToOne
    private Station station;

    @ManyToOne
    private Route route;


    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public RouteEntry() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
