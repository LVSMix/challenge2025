package org.example.challenge.infraestrcture.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.challenge.domain.model.ApiCallLog;
import org.example.challenge.application.repository.ApiCallLogRepository;
import org.example.challenge.domain.response.ErrorResponse;
import org.example.challenge.infraestrcture.constants.Constants;
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


    @Operation(summary = "Retrieve API call logs",
            description = "This endpoint retrieves a paginated list of API call logs.",
            parameters = {
                    @Parameter(name = "page", description = "Page number", required = false),
                    @Parameter(name = "size", description = "Page size", required = false)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content(schema = @Schema(implementation = ErrorResponse.class, example = Constants.ERROR_RESPONSE_400))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class, example = Constants.ERROR_RESPONSE_500)))
    })
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
