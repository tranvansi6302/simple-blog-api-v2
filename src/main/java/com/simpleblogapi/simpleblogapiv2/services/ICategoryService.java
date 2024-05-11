package com.simpleblogapi.simpleblogapiv2.services;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.CategoryCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.CategoryUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    CategoryResponse createCategory(CategoryCreateRequest request);
    List<CategoryResponse> getAllCategories();
    CategoryResponse updateCategory(String id, CategoryUpdateRequest request);
    CategoryResponse getCategory(String id);
    void deleteCategory(String id);
}
