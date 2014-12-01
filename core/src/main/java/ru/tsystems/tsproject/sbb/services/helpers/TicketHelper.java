package ru.tsystems.tsproject.sbb.services.helpers;

import ru.tsystems.tsproject.sbb.database.entity.Trip;

/**
 * Created by apple on 28.11.14.
 */
public class TicketHelper {
    /**
     * Returns formatted information about train.
     * @param trip contains specified train
     * @return string contains trains number, name and route
     */
    public static String getTrainInfo(Trip trip) {
        String tripName = (trip.getTrain().getName() == null || trip.getTrain().getName().isEmpty())
                ? ""
                : trip.getTrain().getName() +  "&nbsp;";
        return trip.getTrain().getId() + "&nbsp;" + tripName + RouteHelper.getRouteName(trip.getRoute());
    }
}
