package ru.tsystems.tsproject.sbb.transferObjects;

import ru.tsystems.tsproject.sbb.services.validators.DateRange;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by apple on 25.10.2014.
 */
public class SearchStationTO {
    @NotNull
    @Pattern(regexp="\\d{2}\\.\\d{2}\\.\\d{4}")
    @DateRange
    private String date;

    @NotNull
    @Size(min = 3, max = 100)
    @Pattern(regexp="[A-Za-z\\p{L}][A-Za-z- \\p{L}]{0,98}[A-Za-z\\p{L}]")
    private String station;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
