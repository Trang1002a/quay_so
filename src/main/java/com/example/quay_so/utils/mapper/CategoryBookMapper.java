package com.example.quay_so.utils.mapper;

import com.example.quay_so.model.entity.CategoriesEntity;
import com.example.quay_so.model.entity.CategoryBookEntity;
import com.example.quay_so.utils.StatusType;

import java.util.Date;
import java.util.UUID;

public class CategoryBookMapper {
    public static CategoryBookEntity maptoCategoryBookEntity(CategoriesEntity categoriesEntity, String bookId) {
        CategoryBookEntity entity = new CategoryBookEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setBookId(bookId);
        entity.setCategoryId(categoriesEntity.getId());
        entity.setStatus(StatusType.ACTIVE.name());
        entity.setCreatedAt(new Date(System.currentTimeMillis()));
        entity.setUpdatedAt(new Date(System.currentTimeMillis()));
        return entity;
    }
}
