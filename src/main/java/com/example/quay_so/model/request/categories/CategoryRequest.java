package com.example.quay_so.model.request.categories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private String id;
    private String name;
    private String description;
    private String status;
}
