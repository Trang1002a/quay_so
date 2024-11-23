package com.example.quay_so.utils.validate;

import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.model.entity.CategoriesEntity;
import com.bookstore.quay_so.model.repository.CategoryRepository;
import com.example.quay_so.model.request.categories.CategoryRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryValidate {

    @Autowired
    private CategoryRepository categoryRepository;

    public void validateRequestCategory(CategoryRequest request) {
        if (StringUtils.isBlank(request.getName())
                || StringUtils.isBlank(request.getDescription())
                || StringUtils.isBlank(request.getStatus())) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        if (checkExistName(request.getName(), request.getId())) {
            throw new ProjectException("", "Tên danh mục đã tồn tại");
        }

    }

    public boolean checkExistName(String name, String id) {
        Optional<CategoriesEntity> entity = categoryRepository.findByName(name);
        return entity.isPresent() && checkId(entity.get().getId(), id);
    }

    private boolean checkId(String id, String requestId) {
        return StringUtils.isBlank(requestId) || !StringUtils.equalsIgnoreCase(id, requestId);
    }
}
