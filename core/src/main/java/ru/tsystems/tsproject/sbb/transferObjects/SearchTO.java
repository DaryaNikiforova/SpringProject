package ru.tsystems.tsproject.sbb.transferObjects;

import ru.tsystems.tsproject.sbb.services.validators.DateRange;
import ru.tsystems.tsproject.sbb.services.validators.TimeBounds;
import ru.tsystems.tsproject.sbb.services.validators.UniqueFields;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by apple on 25.10.2014.
 */
@UniqueFields(first = "stationFrom", second = "stationTo")
@TimeBounds(min = "departure", max = "arrival")
public class SearchTO {

    @NotNull
    @Size(min = 3, max = 100)
    @Pattern(regexp="[A-Za-z\\p{L}][A-Za-z- \\p{L}]{0,98}[A-Za-z\\p{L}]")
    String stationFrom;

    @NotNull
    @Size(min = 3, max = 100)
    @Pattern(regexp="[A-Za-z\\p{L}][A-Za-z- \\p{L}]{0,98}[A-Za-z\\p{L}]")
    String stationTo;

    @NotNull
    @Pattern(regexp="\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}")
    @DateRange
    String departure;

    @NotNull
    @Pattern(regexp="\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}")
    @DateRange
    String arrival;

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
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
}
