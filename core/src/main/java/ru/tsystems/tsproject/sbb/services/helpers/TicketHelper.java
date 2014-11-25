package ru.tsystems.tsproject.sbb.services.helpers;

import ru.tsystems.tsproject.sbb.database.entity.Trip;

/**
 * Implements set of methods to help fill transfer objects fields. Methods need to calculate
 * metrics needed for client data representation.
 * @author Daria Nikiforova
 */
public class TicketHelper {
    private TicketHelper(){}

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

    /**
     * Return formatted value of ticket price.
     * @param price double value of price.
     * @return string value of price.
     */
    public static String formatPrice(double price) {
        return String.valueOf((long) price) + "&nbsp;р.";
    }

}
