package ru.tsystems.tsproject.sbb.transferObjects;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

/**
 * Represents client entity of RouteEntry.
 * @author Daria Nikiforova
 */
public class RouteEntryTO {
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }


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

    private String stationName;
    private int seqNumber;

    @Min(value = 0)
    private int distance;

    @Range(min = 0, max = 1000)
    private int hour;

    @Range(min = 0, max = 59)
    private int minute;
}
