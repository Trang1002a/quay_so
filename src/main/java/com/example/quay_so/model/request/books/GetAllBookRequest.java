package com.example.quay_so.model.request.books;

import com.example.quay_so.model.request.transaction.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllBookRequest {
    private String name;
    private String author;
    private String price;
    private String inventory;
    private List<String> status;
    private List<String> categoriesId;
    private PageRequest pageRequest;
}
