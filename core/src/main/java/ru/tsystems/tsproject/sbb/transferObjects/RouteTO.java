package ru.tsystems.tsproject.sbb.transferObjects;

import org.springframework.util.AutoPopulatingList;
import ru.tsystems.tsproject.sbb.services.validators.IncreasingDistance;
import ru.tsystems.tsproject.sbb.services.validators.IncreasingTime;
import ru.tsystems.tsproject.sbb.services.validators.RouteEntriesCorrectness;
import ru.tsystems.tsproject.sbb.services.validators.UniqueStations;

import javax.validation.constraints.NotNull;

/**
 * Represents client entity of Route.
 * @author Daria Nikiforova
 */
public class RouteTO {

    private int number;

    @NotNull
    @UniqueStations
    @IncreasingDistance
    @IncreasingTime
    @RouteEntriesCorrectness
    private AutoPopulatingList<RouteEntryTO> routeEntries = null;
    private String route;
    private String time;
    private String distance;


    public RouteTO() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public AutoPopulatingList<RouteEntryTO> getRouteEntries() {
        return routeEntries;
    }

    public void setRouteEntries(AutoPopulatingList<RouteEntryTO> routeEntries) {
        this.routeEntries = routeEntries;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}
