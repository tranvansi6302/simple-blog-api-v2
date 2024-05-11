package com.simpleblogapi.simpleblogapiv2.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequest {

    @NotBlank(message = "INVALID_CATEGORY_NAME_REQUIRED")
    private String name;
}
