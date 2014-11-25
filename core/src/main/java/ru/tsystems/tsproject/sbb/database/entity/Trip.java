package ru.tsystems.tsproject.sbb.database.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents trips for train in specified date. On one trip bought many tickets.
 * Trips can have the same route. Uniqueness defines by routes id, trains id and time of departure.
 * @author Daria Nikiforova
 */
@Entity
@NamedQuery(name="Trip.getTripsByRoute", query="select t from Trip t where t.route.id = :id and day(t.departureTime) = day(:date) " +
                                               "and month(t.departureTime) = month(:date) and year(t.departureTime) = year(:date)")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"route_id", "train_id", "departureTime"})})
public class Trip {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private Date departureTime;

    @ManyToOne
    private Train train;

    @ManyToOne
    private Route route;

    @OneToMany(mappedBy = "trip")
    private List<Ticket> tickets;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Trip() {
        tickets = new ArrayList<Ticket>();
    }
}
