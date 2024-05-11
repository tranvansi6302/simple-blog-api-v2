package com.simpleblogapi.simpleblogapiv2.controllers;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.CategoryCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.CategoryUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.ApiResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.CategoryResponse;
import com.simpleblogapi.simpleblogapiv2.services.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getAllCategories())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .result(categoryService.getCategory(id))
                .build());
    }
    @PostMapping("")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @RequestBody @Valid CategoryCreateRequest request) {
        ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>builder()
                .message("Category created successfully")
                .result(categoryService.createCategory(request))
                .build();
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable String id,
            @RequestBody @Valid CategoryUpdateRequest request) {
        ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>builder()
                .message("Category updated successfully")
                .result(categoryService.updateCategory(id, request))
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Category deleted successfully")
                .result(id)
                .build());
    }
}
