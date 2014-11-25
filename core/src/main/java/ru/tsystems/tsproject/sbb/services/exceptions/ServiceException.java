package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Common interface for services exceptions.
 * @author Nikiforova Daria
 */
public class ServiceException extends Exception{
    public ServiceException(String message) {
        super(message);
    }
}
