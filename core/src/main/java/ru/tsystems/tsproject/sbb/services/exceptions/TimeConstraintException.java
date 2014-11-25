package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Fires when difference between date of ticket purchase and trip date is less than 10 minutes.
 * @author Nikiforova Daria
 */
public class TimeConstraintException extends ServiceException{
    public TimeConstraintException(String message) {
        super(message);
    }
}
