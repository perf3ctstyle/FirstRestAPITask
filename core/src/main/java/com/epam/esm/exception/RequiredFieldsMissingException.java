package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RequiredFieldsMissingException extends RuntimeException {

    public RequiredFieldsMissingException(String message) {
        super(message);
    }

    public RequiredFieldsMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredFieldsMissingException(Throwable cause) {
        super(cause);
    }
}