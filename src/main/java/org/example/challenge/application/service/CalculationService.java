package org.example.challenge.application.service;

import org.example.challenge.domain.response.CalculatorResponse;
import org.example.challenge.infraestrcture.client.PercentageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    @Autowired
    private PercentageClient percentageClient;

    public CalculatorResponse calculateWithPercentage(double num1, double num2) {

        double sum = num1 + num2;
        double percentage = percentageClient.getPercentage();
        return CalculatorResponse.builder().result(sum + (sum * (percentage / 100))).build();
    }
}
