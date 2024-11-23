package com.example.quay_so.presentation.controller.admin;

import com.example.quay_so.model.request.categories.CategoryResponse;
import com.example.quay_so.model.request.categories.GetAllCategoryRequest;
import com.example.quay_so.model.request.categories.GetAllCategoryResponse;
import com.example.quay_so.presentation.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Resource
    CategoryService categoryService;

    @PostMapping("/getAll")
    public GetAllCategoryResponse getAll(@RequestBody GetAllCategoryRequest request) {
        return categoryService.getAll(request);
    }

    @GetMapping("/getAllActive")
    public List<CategoryResponse> getAllActive() {
        return categoryService.getAllActive();
    }

    @GetMapping("/{id}")
    public CategoryResponse findById(@PathVariable("id") String id) {
        return categoryService.findById(id);
    }
}
