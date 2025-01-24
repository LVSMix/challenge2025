package org.example.challenge.infraestrcture.adapter.in.web.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.challenge.application.service.ApiCallLogService;
import org.example.challenge.application.service.CalculationService;
import org.example.challenge.domain.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    @Autowired
    private CalculationService calculationService;
    @Autowired
    private ApiCallLogService logService;


    @Operation(summary = "Calculate the result of a mathematical operation",
            description = "This endpoint calculates the result of a mathematical operation based on the provided parameters.",
            parameters = {
                    @Parameter(name = "num1", description = "The first number", required = true),
                    @Parameter(name = "num2", description = "The second number", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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