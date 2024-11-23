package com.example.quay_so.model.request.books;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private String id;
    private String name;
    private String author;
    private String publishYear;
    private String description;
    private String price;
    private String inventory;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String createdAt;
    private String updatedAt;
    private String votes;
    private String image;
    private Integer sold;
    private List<String> categoriesId;
}
