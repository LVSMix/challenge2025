package org.example.challenge.domain.model;
import java.time.LocalDateTime;

public class ApiCallLog {

    private LocalDateTime timestamp;
    private String endpoint;
    private String parameters;
    private String response;
    private String error;

    public ApiCallLog(LocalDateTime timestamp, String endpoint, String parameters, String response, String error) {
        this.timestamp = timestamp;
        this.endpoint = endpoint;
        this.parameters = parameters;
        this.response = response;
        this.error = error;
    }

    // Getters y setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

