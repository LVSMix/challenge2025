package org.example.challenge.application.service;


import lombok.extern.slf4j.Slf4j;
import org.example.challenge.application.repository.ApiCallLogRepository;
import org.example.challenge.domain.model.ApiCallLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ApiCallLogService {
    @Autowired
    private ApiCallLogRepository repository;

    @Async
    public void log(String endpoint, String parameters, String response, String error) {
        try {
            ApiCallLog log = new ApiCallLog();
            log.setTimestamp(LocalDateTime.now());
            log.setEndpoint(endpoint);
            log.setParameters(parameters);
            log.setResponse(response);
            log.setError(error);
            repository.save(log);
        } catch (Exception e) {
            // Si falla, no impacta la ejecución del endpoint invocado
            log.error(e.getMessage());
        }
    }
}
