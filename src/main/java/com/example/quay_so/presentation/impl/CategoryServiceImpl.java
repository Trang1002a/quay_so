package com.example.quay_so.presentation.impl;

import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.model.dto.UserInfoDto;
import com.example.quay_so.model.entity.CategoriesEntity;
import com.example.quay_so.model.repository.CategoryRepository;
import com.example.quay_so.model.request.categories.CategoryResponse;
import com.example.quay_so.model.request.categories.GetAllCategoryRequest;
import com.example.quay_so.model.request.categories.GetAllCategoryResponse;
import com.example.quay_so.model.response.TotalResponse;
import com.example.quay_so.presentation.service.CategoryService;
import com.example.quay_so.utils.StatusType;
import com.example.quay_so.utils.UserInfoService;
import com.example.quay_so.utils.mapper.CategoryMapper;
import com.example.quay_so.utils.validate.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryRepository categoryRepository;

    @Override
    public GetAllCategoryResponse getAll(GetAllCategoryRequest request) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
//        validateGetAllCategoryRequest(request);
        GetAllCategoryResponse response = new GetAllCategoryResponse();
        Sort sort = Sort.by(Sort.Direction.ASC, "updatedAt");
        Pageable pageable;
        try {
            pageable = PageRequest.of(request.getPageRequest().getPage(), request.getPageRequest().getSize(), sort);
        } catch (Exception e) {
            pageable = PageRequest.of(1, 10, sort);
        }
        Page<CategoriesEntity> list = categoryRepository.findCategoriesWithRequest(request.getName(), request.getStatus(), pageable);
        if (list.isEmpty()) {
            return response;
        }
        TotalResponse totalResponse = new TotalResponse();
        totalResponse.setTotalPages(String.valueOf(list.getTotalPages()));
        totalResponse.setTotalElements(String.valueOf(list.getTotalElements()));
        response.setTotalResponse(totalResponse);
        response.setCategoryResponses(list.stream().map(CategoryMapper::mapCategoryEntityToCategoryResponse).collect(Collectors.toList()));
        return response;
    }

    @Override
    public List<CategoryResponse> getAllActive() {
        List<CategoryResponse> responses = new ArrayList<>();
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        List<CategoriesEntity> list = categoryRepository.findAllByStatusOrderByCreatedAtAsc(StatusType.ACTIVE.name());
        if (CollectionUtils.isEmpty(list)) {
            return responses;
        }
        return list.stream().map(CategoryMapper::mapCategoryEntityToCategoryResponse).collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getAllCategoryActive() {
        List<CategoriesEntity> list = categoryRepository.findAllByStatusOrderByCreatedAtAsc(StatusType.ACTIVE.name());
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(CategoryMapper::mapCategoryEntityToCategoryResponse).collect(Collectors.toList());
    }

    @Override
    public CategoryResponse findById(String id) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.ADMIN.name());
        Optional<CategoriesEntity> categoriesEntity = categoryRepository.findById(id);
        if (!categoriesEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return CategoryMapper.mapCategoryEntityToCategoryResponse(categoriesEntity.get());
    }

}
