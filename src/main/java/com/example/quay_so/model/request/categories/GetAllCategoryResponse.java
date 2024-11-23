package com.example.quay_so.model.request.categories;

import com.example.quay_so.model.response.TotalResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCategoryResponse {
    private List<CategoryResponse> categoryResponses;
    private TotalResponse totalResponse;
}
