package com.example.quay_so.presentation.service;

import com.example.quay_so.model.request.accounts.AccountRequest;
import com.example.quay_so.model.request.accounts.UserRequest;
import com.example.quay_so.model.request.books.BookRequest;
import com.example.quay_so.model.request.categories.CategoryRequest;
import com.example.quay_so.model.request.transaction.*;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.transaction.TransPendingResponse;

public interface TransactionService {
    ResStatus createUser(UserRequest userRequest);

    ResStatus createAccount(AccountRequest accountRequest);

    TransPendingResponse getTransPending(TransPendingRequest transPendingRequest);

    ResStatus createCategory(CategoryRequest categoryRequest);

    ResStatus createBook(BookRequest bookRequest);

    ResStatus updateUser(UserRequest userRequest);

    ResStatus updateAccount(AccountRequest accountRequest);

    ResStatus updateCategory(CategoryRequest categoryRequest);

    ResStatus updateBook(BookRequest bookRequest);

    VerifyResponse verify(VerifyRequest verifyRequest);

    ResStatus confirm(ConfirmRequest confirmRequest);

    ResStatus cancel(CancelRequest cancelRequest);
}
