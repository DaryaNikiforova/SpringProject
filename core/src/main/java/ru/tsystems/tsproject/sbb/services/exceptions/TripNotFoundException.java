package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Created by apple on 26.11.14.
 */
public class TripNotFoundException extends ServiceException {
    public TripNotFoundException(String message) {
        super(message);
    }
}
