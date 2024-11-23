package com.example.quay_so.presentation.controller.customer;

import com.example.quay_so.model.request.categories.CategoryResponse;
import com.example.quay_so.presentation.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/customer/category")
public class CategoryCustomerController {
    @Resource
    CategoryService categoryService;

    @GetMapping("/getAll")
    public List<CategoryResponse> getAllCategoryActive() {
        return categoryService.getAllCategoryActive();
    }
}
