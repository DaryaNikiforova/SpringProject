package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Fires when attempt to add existing user into database.
 */
public class UserAlreadyExistException extends ServiceException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
