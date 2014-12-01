package ru.tsystems.tsproject.sbb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by apple on 26.11.14.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Incorrect request")
public class BadRequestException extends ControllerException {
}
