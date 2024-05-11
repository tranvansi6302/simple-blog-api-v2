package com.simpleblogapi.simpleblogapiv2.dtos.requests;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class IntrospectRequest {
    @NotBlank(message = "TOKEN_REQUIRED")
    private String token;
}
