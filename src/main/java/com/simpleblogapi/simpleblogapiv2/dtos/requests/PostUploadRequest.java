package com.simpleblogapi.simpleblogapiv2.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUploadRequest {
    private String thumbnail;
}
