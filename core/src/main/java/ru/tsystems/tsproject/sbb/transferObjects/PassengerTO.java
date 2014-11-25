package ru.tsystems.tsproject.sbb.transferObjects;

/**
 * Represents client Passenger entity.
 * @author Daria Nikiforova
 */
public class PassengerTO {

    private String name;

    private String surname;

    private String birthDate;

    private int seat;

    private String passRoute;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getPassRoute() {
        return passRoute;
    }

    public void setPassRoute(String passRoute) {
        this.passRoute = passRoute;
    }

}
