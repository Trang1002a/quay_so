package com.example.quay_so.presentation.service;

import com.example.quay_so.model.request.categories.CategoryResponse;
import com.example.quay_so.model.request.categories.GetAllCategoryRequest;
import com.example.quay_so.model.request.categories.GetAllCategoryResponse;

import java.util.List;

public interface CategoryService {

    GetAllCategoryResponse getAll(GetAllCategoryRequest request);

    CategoryResponse findById(String id);

    List<CategoryResponse> getAllActive();

    List<CategoryResponse> getAllCategoryActive();
}
