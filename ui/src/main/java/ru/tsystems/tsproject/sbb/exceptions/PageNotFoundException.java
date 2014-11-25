package ru.tsystems.tsproject.sbb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by apple on 25.11.14.
 */

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Requested resource was not found")
public class PageNotFoundException extends ControllerException {
    public PageNotFoundException(int id) {}
}
