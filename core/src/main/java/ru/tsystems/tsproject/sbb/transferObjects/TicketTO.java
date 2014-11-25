package ru.tsystems.tsproject.sbb.transferObjects;

import java.util.*;

/**
 * Represents client entity of Ticket.
 * @author Daria Nikiforova
 */
public class TicketTO {

    private String route;
    private String stationFrom;
    private String stationTo;
    private int routeId;
    private int tripId;
    private String trip;
    private Date departure;
    private Date arrival;
    private String userName;
    private String userSurname;
    private String birthDate;
    private int seatNumber = 0;
    private int rateType;
    private String rateName;
    private int trainRate;
    private String login;
    //private Map<Long, String> services;
    List<String> services;
    private List<Integer> seats;
    private Map<Long, String> rateTypes;
    private double price;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public String getStationTo() {
        return stationTo;
    }

    public void setStationTo(String stationTo) {
        this.stationTo = stationTo;
    }

    public String getStationFrom() {
        return stationFrom;
    }

    public void setStationFrom(String stationFrom) {
        this.stationFrom = stationFrom;
    }

    public int getTrainRate() {
        return trainRate;
    }

    public void setTrainRate(int trainRate) {
        this.trainRate = trainRate;
    }

    public TicketTO() {
        seats = new ArrayList<Integer>();
        services = new ArrayList<String>();
        rateTypes = new HashMap<Long, String>();
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getRateType() {
        return rateType;
    }

    public void setRateType(int rateType) {
        this.rateType = rateType;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public List<Integer> getSeats() {
        return seats;
    }

    public void setSeats(List<Integer> seats) {
        this.seats = seats;
    }

    public Map<Long,String> getRateTypes() {
        return rateTypes;
    }

    public void setRateTypes(Map<Long,String> rateTypes) {
        this.rateTypes = rateTypes;
    }

}
