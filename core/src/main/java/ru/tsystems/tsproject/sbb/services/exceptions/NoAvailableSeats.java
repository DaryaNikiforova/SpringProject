package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Created by apple on 28.11.14.
 */
public class NoAvailableSeats extends ServiceException {
    public NoAvailableSeats(String message) {
        super(message);
    }
}
