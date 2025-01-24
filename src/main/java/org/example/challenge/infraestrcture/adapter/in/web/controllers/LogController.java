package org.example.challenge.infraestrcture.adapter.in.web.controllers;

import org.example.challenge.domain.model.ApiCallLog;
import org.example.challenge.domain.repository.ApiCallLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private ApiCallLogRepository repository;

    @GetMapping
    public List<ApiCallLog> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<ApiCallLog> allLogs = repository.findAll();
        return allLogs.stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }
}
