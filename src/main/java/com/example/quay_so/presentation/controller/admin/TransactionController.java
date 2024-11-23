package com.example.quay_so.presentation.controller.admin;

import com.example.quay_so.model.request.accounts.AccountRequest;
import com.example.quay_so.model.request.accounts.UserRequest;
import com.example.quay_so.model.request.books.BookRequest;
import com.example.quay_so.model.request.categories.CategoryRequest;
import com.example.quay_so.model.request.transaction.*;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.transaction.TransPendingResponse;
import com.example.quay_so.presentation.service.TransactionService;
import com.example.quay_so.utils.MinIOUploader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/transaction")
public class TransactionController {
    @Resource
    TransactionService transactionService;

    @Resource
    MinIOUploader minIOUploader;

    @PostMapping("/createUser")
    public ResStatus createUser(@RequestBody UserRequest userRequest) {
        return transactionService.createUser(userRequest);
    }
    @PostMapping("/updateUser")
    public ResStatus updateUser(@RequestBody UserRequest userRequest) {
        return transactionService.updateUser(userRequest);
    }

    @PostMapping("/createAccount")
    public ResStatus createAccount(@RequestBody AccountRequest accountRequest) {
        return transactionService.createAccount(accountRequest);
    }

    @PostMapping("/updateAccount")
    public ResStatus updateAccount(@RequestBody AccountRequest accountRequest) {
        return transactionService.updateAccount(accountRequest);
    }

    @PostMapping("/createCategory")
    public ResStatus createCategory(@RequestBody CategoryRequest categoryRequest) {
        return transactionService.createCategory(categoryRequest);
    }

    @PostMapping("/updateCategory")
    public ResStatus updateCategory(@RequestBody CategoryRequest categoryRequest) {
        return transactionService.updateCategory(categoryRequest);
    }

    @PostMapping("/createBook")
    public ResStatus createBook(@RequestParam("name") String name,
                                @RequestParam("author") String author,
                                @RequestParam("publishYear") String publishYear,
                                @RequestParam("description") String description,
                                @RequestParam("price") String price,
                                @RequestParam("inventory") String inventory,
                                @RequestParam("status") String status,
                                @RequestParam("categoriesId") List<String> categoriesId,
                                @RequestParam("file") MultipartFile file) {

        String id = UUID.randomUUID().toString();
        String imageUrl = minIOUploader.getMinioUrl(file,id);
        BookRequest bookRequest = new BookRequest();
        bookRequest.setName(name);
        bookRequest.setAuthor(author);
        bookRequest.setPublishYear(publishYear);
        bookRequest.setDescription(description);
        bookRequest.setPrice(price);
        bookRequest.setInventory(inventory);
        bookRequest.setStatus(status);
        bookRequest.setCategoriesId(categoriesId);
        bookRequest.setImageUrl(imageUrl);
        return transactionService.createBook(bookRequest);
    }

    @PostMapping("/updateBook")
    public ResStatus updateBook(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("author") String author,
            @RequestParam("publishYear") String publishYear,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam("inventory") String inventory,
            @RequestParam("status") String status,
            @RequestParam("categoriesId") List<String> categoriesId,
            @RequestParam("file") MultipartFile file) {
        String imageUrl = minIOUploader.getMinioUrl(file,id);
        BookRequest bookRequest = new BookRequest(id,name,author,publishYear,description,price,inventory,status,categoriesId,imageUrl);
        return transactionService.updateBook(bookRequest);
    }

    @PostMapping("/verify")
    public VerifyResponse verify(@RequestBody VerifyRequest verifyRequest) {
        return transactionService.verify(verifyRequest);
    }

    @PostMapping("/cancel")
    public ResStatus cancel(@RequestBody CancelRequest cancelRequest) {
        return transactionService.cancel(cancelRequest);
    }

    @PostMapping("/confirm")
    public ResStatus confirm(@RequestBody ConfirmRequest confirmRequest) {
        return transactionService.confirm(confirmRequest);
    }

    @PostMapping("/pending")
    public TransPendingResponse getTransPending(@RequestBody TransPendingRequest transPendingRequest) {
        return transactionService.getTransPending(transPendingRequest);
    }
}
