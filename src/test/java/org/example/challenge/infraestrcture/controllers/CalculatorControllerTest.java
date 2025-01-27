package org.example.challenge.infraestrcture.controllers;

import org.example.challenge.application.service.ApiCallLogService;
import org.example.challenge.application.service.CalculationService;
import org.example.challenge.domain.response.CalculatorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CalculatorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CalculationService calculationService;

    @Mock
    private ApiCallLogService logService;

    @InjectMocks
    private CalculatorController calculatorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(calculatorController).build();
    }

    @Test
    void calculate_ShouldReturnResult() throws Exception {
        when(calculationService.calculateWithPercentage(anyDouble(), anyDouble())).thenReturn(new CalculatorResponse(5.0));

        mockMvc.perform(get("/api/calculator/calculate")
                        .param("num1", "2.0")
                        .param("num2", "3.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(5.0));
    }

    @Test
    void calculate_ShouldReturnBadRequest_WhenMissingParams() throws Exception {
        mockMvc.perform(get("/api/calculator/calculate")
                        .param("num1", "2.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculate_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
        when(calculationService.calculateWithPercentage(anyDouble(), anyDouble())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/calculator/calculate")
                        .param("num1", "2.0")
                        .param("num2", "3.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
