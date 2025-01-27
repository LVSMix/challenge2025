package org.example.challenge.domain.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Schema(description = "Calculator response model")
@Builder
public class CalculatorResponse {

    @Schema(description = "Result of Calculate", example = "10.0")
    private double result = 0;
}
