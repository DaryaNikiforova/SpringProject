package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Fires when trying add to database existing station.
 * @author Nikiforova Daria
 */
public class StationAlreadyExistException extends ServiceException {
    public StationAlreadyExistException(String message) {
        super(message);
    }
}
