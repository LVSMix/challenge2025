package org.example.challenge.domain.exception;

public class ExternalServiceRetryException extends RuntimeException {
    public ExternalServiceRetryException(String message) {
        super(message);
    }
}
