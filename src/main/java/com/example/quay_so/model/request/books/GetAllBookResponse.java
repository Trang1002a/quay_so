package com.example.quay_so.model.request.books;

import com.example.quay_so.model.response.TotalResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllBookResponse {
    private List<BookResponse> bookResponseList;
    private TotalResponse totalResponse;
}
