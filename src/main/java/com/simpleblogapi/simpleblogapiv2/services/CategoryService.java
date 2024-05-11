package com.simpleblogapi.simpleblogapiv2.services;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.CategoryCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.CategoryUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.CategoryResponse;
import com.simpleblogapi.simpleblogapiv2.entities.Category;
import com.simpleblogapi.simpleblogapiv2.exceptions.AppException;
import com.simpleblogapi.simpleblogapiv2.exceptions.ErrorCode;
import com.simpleblogapi.simpleblogapiv2.mappers.CategoryMapper;
import com.simpleblogapi.simpleblogapiv2.repositories.CategoryRepository;
import com.simpleblogapi.simpleblogapiv2.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTS);
        }
        Category category = categoryMapper.toCategory(request);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(String id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        if(!request.getName().isEmpty()) {
            categoryMapper.toCategory(category, request);
        }

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        if (postRepository.existsByCategory(category)) {
            throw new AppException(ErrorCode.CATEGORY_HAS_POST);
        }
        categoryRepository.delete(category);
    }
}
