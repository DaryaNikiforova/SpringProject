package ru.tsystems.tsproject.sbb.database.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents train. Train has many trips in different days.
 * @author Nikiforova Daria
 */
@Entity
public class Train {
    @Id
    private int id;

    private int seatCount;

    private String name;

    @OneToOne
    private Rate rate;

    @OneToMany(mappedBy = "train")
    private List<Trip> trips;

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Train() {
        trips = new ArrayList<Trip>();
    }

    public Train(int id, int seatCount, String name, Rate rate) {
        this.seatCount = seatCount;
        this.id = id;
        this.name = name;
        this.rate = rate;
    }


    public Rate getRate() {
        return rate;
    }

}
