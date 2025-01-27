package org.example.challenge.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Error response model")
public class ErrorResponse {

    @Schema(description = "Error message", example = "Invalid request")
    private String message;
}
