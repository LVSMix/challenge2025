package org.example.challenge.infraestrcture.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.challenge.application.service.ApiCallLogService;
import org.example.challenge.application.service.CalculationService;
import org.example.challenge.domain.response.CalculatorResponse;
import org.example.challenge.domain.response.ErrorResponse;
import org.example.challenge.infraestrcture.constants.Constants;
import org.example.challenge.infraestrcture.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = CalculatorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class, example = Constants.ERROR_RESPONSE_400))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class, example = Constants.ERROR_RESPONSE_500)))
    })
    @GetMapping("/calculate")
    public CalculatorResponse calculate(
            @RequestParam double num1,
            @RequestParam double num2
    ) {
        if (Objects.isNull(num1)  || Objects.isNull(num2)) {
            throw new BadRequestException("Los parámetros num1 y num2 son obligatorios.");
        }
        return calculationService.calculateWithPercentage(num1, num2);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Internal server error" + ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}