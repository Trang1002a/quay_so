package com.example.quay_so.model.request.categories;

import com.example.quay_so.model.request.transaction.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCategoryRequest {
    private String name;
    private List<String> status;
    private PageRequest pageRequest;
}
