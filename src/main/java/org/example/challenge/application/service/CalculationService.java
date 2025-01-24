package org.example.challenge.application.service;

import org.example.challenge.infrestrcture.adapter.out.client.PercentageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    @Autowired
    private PercentageClient percentageClient;

    public double calculateWithPercentage(double num1, double num2) {

        double sum = num1 + num2;
        double percentage = percentageClient.getPercentage();
        return sum + (sum * (percentage / 100));
    }
}
