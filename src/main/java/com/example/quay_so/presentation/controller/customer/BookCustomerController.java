package com.example.quay_so.presentation.controller.customer;

import com.bookstore.quay_so.model.request.books.*;
import com.example.quay_so.model.request.books.*;
import com.example.quay_so.presentation.service.BookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/customer/book")
public class BookCustomerController {
    @Resource
    BookService bookService;

    @PostMapping("/getAll")
    public GetAllBookResponse getAllBookActive(@RequestBody GetAllBookRequest request) {
        return bookService.getAllBookActive(request);
    }

    @GetMapping("/details/{id}")
    public BookResponse findByIdPublic(@PathVariable("id") String id) {
        return bookService.findByIdPublic(id);
    }

    @GetMapping("/votes/{id}")
    public VotesByIdResponse votesByBookId(@PathVariable("id") String id) {
        return bookService.votesByBookId(id);
    }

    @GetMapping("/comments/{id}")
    public CommentsByIdResponse commentsByBookId(@PathVariable("id") String id) {
        return bookService.commentsByBookId(id);
    }

    @GetMapping("/popular")
    public List<BookResponse> getAllPopular() {
        return bookService.getAllPopular();
    }

        @GetMapping("/bestSelling")
    public List<BookResponse> getAllBestSelling() {
        return bookService.getAllBestSelling();
    }
}
