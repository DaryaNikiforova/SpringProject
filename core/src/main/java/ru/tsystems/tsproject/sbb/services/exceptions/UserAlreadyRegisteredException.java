package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Fires when the same user trying buy train ticket on trip, which he already registered.
 * @author Nikiforova Daria
 */
public class UserAlreadyRegisteredException extends ServiceException{
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
