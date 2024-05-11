package com.simpleblogapi.simpleblogapiv2.dtos.responses;

import com.simpleblogapi.simpleblogapiv2.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private String id;
    private String title;
    private String thumbnail;
    private String content;
    private PostStatus status;
    private CategoryResponse category;
    private UserResponse author;
}
