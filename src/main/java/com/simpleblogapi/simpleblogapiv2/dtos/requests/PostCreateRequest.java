package com.simpleblogapi.simpleblogapiv2.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simpleblogapi.simpleblogapiv2.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {
    @NotBlank(message = "INVALID_POST_TITLE_REQUIRED")
    private String title;
    private String content;

    @JsonProperty("category_id")
    @NotBlank(message = "CATEGORY_ID_REQUIRED")
    private String categoryId;
}
