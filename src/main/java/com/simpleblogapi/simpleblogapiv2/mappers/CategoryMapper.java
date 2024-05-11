package com.simpleblogapi.simpleblogapiv2.mappers;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.CategoryCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.CategoryUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.UserUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.CategoryResponse;
import com.simpleblogapi.simpleblogapiv2.entities.Category;
import com.simpleblogapi.simpleblogapiv2.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreateRequest request);

    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toCategory(@MappingTarget Category category, CategoryUpdateRequest request);
}
