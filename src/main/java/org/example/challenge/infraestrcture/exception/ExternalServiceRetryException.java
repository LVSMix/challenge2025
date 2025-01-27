package org.example.challenge.infraestrcture.exception;

public class ExternalServiceRetryException extends RuntimeException {
    public ExternalServiceRetryException(String message) {
        super(message);
    }
}
