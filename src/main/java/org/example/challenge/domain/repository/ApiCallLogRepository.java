package org.example.challenge.domain.repository;

import org.example.challenge.domain.model.ApiCallLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiCallLogRepository extends JpaRepository<ApiCallLog, Long> {
}
