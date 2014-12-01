package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Created by apple on 26.11.14.
 */
public class SeatAlreadyRegisteredException extends ServiceException {
    public SeatAlreadyRegisteredException(String message) {
        super(message);
    }
}
