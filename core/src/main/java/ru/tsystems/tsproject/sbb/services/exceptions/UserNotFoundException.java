package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Fires when request non existing user.
 * @author Nikiforova Daria
 */
public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
