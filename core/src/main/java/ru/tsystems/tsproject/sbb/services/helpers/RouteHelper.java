package ru.tsystems.tsproject.sbb.services.helpers;

import ru.tsystems.tsproject.sbb.database.entity.Route;
import ru.tsystems.tsproject.sbb.database.entity.RouteEntry;

import java.util.*;

/**
 * Implements set of methods to help fill transfer objects fields. Methods need to calculate
 * metrics needed for client data representation.
 * @author Daria Nikiforova
 */
public class RouteHelper {

    private RouteHelper(){}

    /**
     * Calculates distance between first and last stations of route.
     * @param route contains route entries with needed stations.
     * @return distance value.
     */
    public static String getRouteDistance(Route route) {
        List<RouteEntry> list = route.getRouteEntries();
        if(list == null || list.isEmpty()) {
            return "0";
        }
        Collections.sort(list, new Comparator<RouteEntry>() {
            public int compare(RouteEntry o1, RouteEntry o2) {
                return o1.getSeqNumber() - o2.getSeqNumber();
            }
        });

        return Double.toString(list.get(list.size() - 1).getDistance());
    }

    /**
     * Calculates travel time between first and last stations of route.
     * @param route contains route entries with needed stations.
     * @return string with formatted time - hours and minutes.
     */
    public static String getRouteTime(Route route) {
        List<RouteEntry> list = route.getRouteEntries();
        if(list == null || list.isEmpty()) {
            return "0&nbsp;ч.&nbsp;0&nbsp;м.";
        }
        Collections.sort(list, new Comparator<RouteEntry>() {
            public int compare(RouteEntry o1, RouteEntry o2) {
                return o1.getSeqNumber() - o2.getSeqNumber();
            }
        });
        return list.get(list.size() - 1).getHour() + "&nbsp;ч.&nbsp;" + list.get(list.size() - 1).getMinute() + "&nbsp;м.";
    }

    /**
     * Calculates travel time between two specified stations of route.
     * @param route contains route entries with needed stations.
     * @param stationFrom route beginning station.
     * @param stationTo route ending station.
     * @return string with formatted time - hours and minutes.
     */
    public static String getRouteTime(Route route, String stationFrom, String stationTo) {
        List<RouteEntry> list = route.getRouteEntries();
        if(list == null || list.isEmpty()) {
            return "0&nbsp;ч.&nbsp;0&nbsp;м.";
        }
        Collections.sort(list, new Comparator<RouteEntry>() {
            public int compare(RouteEntry o1, RouteEntry o2) {
                return o1.getSeqNumber() - o2.getSeqNumber();
            }
        });
        int hourFrom = 0;
        int minuteFrom = 0;

        Calendar calendar = Calendar.getInstance();
        for (RouteEntry r : list) {
            if (r.getStation().getName().equals(stationFrom)) {
                hourFrom = r.getHour();
                minuteFrom = r.getMinute();
            }
            if (r.getStation().getName().equals(stationTo)) {
                calendar.set(Calendar.HOUR,r.getHour());
                calendar.set(Calendar.MINUTE, r.getMinute());
            }
        }
        calendar.add(Calendar.HOUR, - hourFrom);
        calendar.add(Calendar.MINUTE, - minuteFrom);
        return calendar.get(Calendar.HOUR) + "&nbsp;ч.&nbsp;" + calendar.get(Calendar.MINUTE) + "&nbsp;м.";
    }

    /** Returns string with route beginning station and rout ending station.
     * @param route contains route entries with needed stations.
     * @return string represents a short routes record.
     */
    public static String getRouteName (Route route) {
        List<RouteEntry> list = route.getRouteEntries();
        if(list == null || list.isEmpty()) {
            return "";
        }
        Collections.sort(list, new Comparator<RouteEntry>() {
            public int compare(RouteEntry o1, RouteEntry o2) {
                return o1.getSeqNumber() - o2.getSeqNumber();
            }
        });
        return list.get(0).getStation().getName() + "&nbsp;→&nbsp;" + list.get(list.size() - 1).getStation().getName();
    }

    /** Returns date of arrival for specified trip.
     * @param route contains route entries with needed stations.
     * @param departureDate specifies when being trains departure.
     * @return arrival date.
     */
    public static Date getArrivalDate(Route route, Date departureDate) {
        List<RouteEntry> list = route.getRouteEntries();
        if(list == null || list.isEmpty()) {
            return null;
        }
        Collections.sort(list, new Comparator<RouteEntry>() {
            public int compare(RouteEntry o1, RouteEntry o2) {
                return o1.getSeqNumber() - o2.getSeqNumber();
            }
        });
        return TimeHelper.addHours(departureDate, list.get(list.size() - 1).getHour(), list.get(list.size() - 1).getMinute());
    }

}
