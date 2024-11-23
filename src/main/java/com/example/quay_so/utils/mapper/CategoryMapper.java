package com.example.quay_so.utils.mapper;

import com.example.quay_so.model.entity.CategoriesEntity;
import com.example.quay_so.model.request.categories.CategoryResponse;

public class CategoryMapper {
    public static CategoryResponse mapCategoryEntityToCategoryResponse(CategoriesEntity categoriesEntity) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(categoriesEntity.getId());
        categoryResponse.setName(categoriesEntity.getName());
        categoryResponse.setDescription(categoriesEntity.getDescription());
        categoryResponse.setStatus(categoriesEntity.getStatus());
        categoryResponse.setCreatedAt(categoriesEntity.getCreatedAt().toString());
        categoryResponse.setUpdatedAt(categoriesEntity.getUpdatedAt().toString());
        categoryResponse.setCreatedBy(categoriesEntity.getCreatedBy());
        categoryResponse.setUpdatedBy(categoriesEntity.getUpdatedBy());
        return categoryResponse;
    }
}
