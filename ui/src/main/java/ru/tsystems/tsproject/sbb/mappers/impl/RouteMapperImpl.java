package ru.tsystems.tsproject.sbb.mappers.impl;

import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.services.helpers.NumberHelper;
import ru.tsystems.tsproject.sbb.transferObjects.RouteEntryTO;
import ru.tsystems.tsproject.sbb.transferObjects.RouteTO;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by apple on 23.10.2014.
 */
public class RouteMapperImpl implements Mapper<RouteTO> {
    @Override
    public RouteTO map(HttpServletRequest request) {
        int i = 1;
        RouteTO route = new RouteTO();
        while (request.getParameter("name" + i) != null) {
            RouteEntryTO routeEntry = new RouteEntryTO();
            routeEntry.setStationName(request.getParameter("name" + i).trim());
            if (i == 1) {
                routeEntry.setHour(0);
                routeEntry.setMinute(0);
                routeEntry.setDistance(0);
            }
            else {
                String hour = request.getParameter("hour" + i);
                String minute = request.getParameter("minute" + i);
                String distance = request.getParameter("distance" + i);
                routeEntry.setHour(hour != null && !hour.isEmpty() && NumberHelper.tryParseInt(hour)? Integer.parseInt(hour) : -1);
                routeEntry.setMinute(minute != null && !minute.isEmpty() && NumberHelper.tryParseInt(minute)? Integer.parseInt(minute) : -1);
                routeEntry.setDistance(distance != null && !distance.isEmpty() && NumberHelper.tryParseInt(distance)? Integer.parseInt(distance) : -1);
            }
            routeEntry.setSeqNumber(i);
            route.getRouteEntries().add(routeEntry);
            i++;
        }
        return route;
    }
}
