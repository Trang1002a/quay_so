package com.example.quay_so.presentation.controller.admin;

import com.example.quay_so.model.request.books.BookResponse;
import com.example.quay_so.model.request.books.GetAllBookRequest;
import com.example.quay_so.model.request.books.GetAllBookResponse;
import com.example.quay_so.model.request.books.TotalBookResponse;
import com.example.quay_so.presentation.service.BookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/book")
public class BookController {

    @Resource
    BookService bookService;

    @PostMapping("/getAll")
    public GetAllBookResponse getAll(@RequestBody GetAllBookRequest request) {
        return bookService.getAll(request);
    }

    @GetMapping("/{id}")
    public BookResponse findById(@PathVariable("id") String id) {
        return bookService.findById(id);
    }

    @GetMapping("/total")
    public TotalBookResponse getTotalBook() {
        return bookService.getTotalBook();
    }
}
