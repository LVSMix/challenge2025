package org.example.challenge.application.service;

import org.example.challenge.domain.model.ApiCallLog;
import org.example.challenge.domain.repository.ApiCallLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApiCallLogService {
    @Autowired
    private ApiCallLogRepository repository;

    @Async
    public void log(String endpoint, String parameters, String response, String error) {
        try {
            ApiCallLog log = new ApiCallLog(
                    LocalDateTime.now(),
                    endpoint,
                    parameters,
                    response,
                    error
            );
            repository.save(log);
        } catch (Exception e) {
            // Si falla, no impacta la ejecución del endpoint invocado
            e.printStackTrace();
        }
    }
}
