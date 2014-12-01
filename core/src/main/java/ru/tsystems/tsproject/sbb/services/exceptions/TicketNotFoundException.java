package ru.tsystems.tsproject.sbb.services.exceptions;

/**
 * Created by apple on 28.11.14.
 */
public class TicketNotFoundException extends ServiceException {
    public TicketNotFoundException(String message) {
        super(message);
    }
}
