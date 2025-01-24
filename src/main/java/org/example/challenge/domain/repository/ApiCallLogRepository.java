package org.example.challenge.domain.repository;


import org.example.challenge.domain.model.ApiCallLog;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class ApiCallLogRepository {

    private final List<ApiCallLog> logStorage = Collections.synchronizedList(new ArrayList<>());

    public void save(ApiCallLog log) {
        logStorage.add(log);
    }

    public List<ApiCallLog> findAll() {
        return new ArrayList<>(logStorage);
    }
}
