package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Fires when trying add existing train into database.
 * @author Nikiforova Daria
 */
public class TrainAlreadyExistException extends ServiceException {
    public TrainAlreadyExistException(String message) {
        super(message);
    }
}
