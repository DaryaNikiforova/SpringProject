package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Fires when requested non existing station.
 * @author Nikiforova Daria
 */

public class StationNotFoundException extends ServiceException {
    public StationNotFoundException(String message) {
        super(message);
    }
}
