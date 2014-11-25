package ru.tsystems.tsproject.sbb.database.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represent passengers train ticket. One passenger can buy more than one ticket at a time, but
 * not for one trip. Each ticket uses one rate for price calculation. Uniqueness defines by sets -
 * "trip id, user id" and "seat number, trip id".
 * @author Nikiforova Daria
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"trip_id","user_id"}),
                            @UniqueConstraint(columnNames = {"seat","trip_id"})})
public class Ticket {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private Date date;

    private int seat;

    private double price;

    @ManyToOne
    private User user;

    @ManyToOne
    private Station stationTo;

    @ManyToOne
    private Station stationFrom;

    @ManyToOne
    private Trip trip;


    @OneToOne
    private Rate rate;


    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "ticket_service")
    private List<Service> services;



    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Station getStationTo() {
        return stationTo;
    }

    public void setStationTo(Station stationTo) {
        this.stationTo = stationTo;
    }

    public Station getStationFrom() {
        return stationFrom;
    }

    public void setStationFrom(Station stationFrom) {
        this.stationFrom = stationFrom;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Ticket() {
        this.services = new ArrayList<Service>();
    }
}
