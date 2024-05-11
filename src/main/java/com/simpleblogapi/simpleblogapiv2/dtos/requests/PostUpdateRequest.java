package com.simpleblogapi.simpleblogapiv2.dtos.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simpleblogapi.simpleblogapiv2.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostUpdateRequest {
    private String title;
    private String content;
    private PostStatus status;

    @JsonProperty("category_id")
    private String categoryId;
}
