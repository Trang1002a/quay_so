package com.example.quay_so.utils.mapper;

import com.example.quay_so.model.entity.BooksEntity;
import com.example.quay_so.model.request.books.BookResponse;

import java.util.List;

public class BookMapper {
    public static BookResponse mapBookEntityToBookResponse(BooksEntity entity, List<String> categoriesId, Double votes) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(entity.getId());
        bookResponse.setName(entity.getName());
        bookResponse.setAuthor(entity.getAuthor());
        bookResponse.setPublishYear(entity.getPublishYear());
        bookResponse.setDescription(entity.getDescription());
        bookResponse.setPrice(entity.getPrice().toString());
        bookResponse.setInventory(entity.getInventory().toString());
        bookResponse.setStatus(entity.getStatus());
        bookResponse.setCreatedAt(entity.getCreatedAt().toString());
        bookResponse.setUpdatedAt(entity.getUpdatedAt().toString());
        bookResponse.setCreatedBy(entity.getCreatedBy());
        bookResponse.setUpdatedBy(entity.getUpdatedBy());
        bookResponse.setCategoriesId(categoriesId);
        bookResponse.setVotes(votes.toString());
        bookResponse.setImage(entity.getImage());
        bookResponse.setSold(entity.getSold());
        return bookResponse;
    }
}
