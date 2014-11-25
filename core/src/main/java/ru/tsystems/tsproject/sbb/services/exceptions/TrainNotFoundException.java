package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Created by apple on 26.11.14.
 */
public class TrainNotFoundException extends ServiceException {
    public TrainNotFoundException(String message) {
        super(message);
    }
}
