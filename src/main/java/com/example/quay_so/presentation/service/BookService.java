package com.example.quay_so.presentation.service;

import com.example.quay_so.model.request.books.*;

import java.util.List;

public interface BookService {
    GetAllBookResponse getAll(GetAllBookRequest request);

    BookResponse findById(String id);

    GetAllBookResponse getAllBookActive(GetAllBookRequest request);

    BookResponse findByIdPublic(String id);

    VotesByIdResponse votesByBookId(String id);

    CommentsByIdResponse commentsByBookId(String id);

    List<BookResponse> getAllPopular();

    List<BookResponse> getAllBestSelling();

    TotalBookResponse getTotalBook();
}
