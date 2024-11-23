package com.example.quay_so.presentation.service;

import com.example.quay_so.model.request.accounts.AccountRequest;
import com.example.quay_so.model.request.accounts.ForgotPasswordRequest;
import com.example.quay_so.model.request.accounts.UpdatePasswordRequest;
import com.example.quay_so.model.request.transaction.ConfirmCustomerRequest;
import com.example.quay_so.model.request.transaction.VerifyCustomerResponse;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.accounts.AccountResponse;

public interface AccountService {

    AccountResponse info();

    ResStatus create(AccountRequest accountRequest);

    ResStatus update(AccountRequest accountRequest);

    VerifyCustomerResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    ResStatus confirmOTP(ConfirmCustomerRequest confirmRequest);

    ResStatus updatePassword(UpdatePasswordRequest request);
}
