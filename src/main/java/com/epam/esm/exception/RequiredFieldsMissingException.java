package com.epam.esm.exception;

public class RequiredFieldsMissingException extends Exception {

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
