package org.example.challenge.infrestrcture.adapter.in.web.controllers;


import org.example.challenge.domain.exception.BadRequestException;
import org.example.challenge.application.service.ApiCallLogService;
import org.example.challenge.application.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    @Autowired
    private CalculationService calculationService;
    @Autowired
    private ApiCallLogService logService;

    @GetMapping("/calculate")
    public double calculate(
            @RequestParam double num1,
            @RequestParam double num2
    ) {
        double result = 0;
        String error = null;

        if (Objects.isNull(num1)  || Objects.isNull(num2)) {
            throw new BadRequestException("Los parámetros num1 y num2 son obligatorios.");
        }


        try {
            result = calculationService.calculateWithPercentage(num1, num2);
        } catch (Exception e) {
            error = e.getMessage();
        }

        // Registrar la llamada de forma asíncrona
        logService.log(
                "/api/calculator/calculate",
                String.format("num1=%f&num2=%f", num1, num2),
                error == null ? String.valueOf(result) : null,
                error
        );

        if (error != null) {
            throw new RuntimeException(error);
        }

        return result;
    }
}