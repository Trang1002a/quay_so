package com.example.quay_so.presentation.controller.customer;

import com.example.quay_so.model.request.accounts.AccountRequest;
import com.example.quay_so.model.request.accounts.ForgotPasswordRequest;
import com.example.quay_so.model.request.accounts.UpdatePasswordRequest;
import com.example.quay_so.model.request.transaction.ConfirmCustomerRequest;
import com.example.quay_so.model.request.transaction.VerifyCustomerResponse;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.accounts.AccountResponse;
import com.example.quay_so.presentation.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/customer/account")
public class AccountCustomerController {

    @Resource
    AccountService accountService;

    @GetMapping("/info")
    public AccountResponse info() {
        return accountService.info();
    }

    @PostMapping("/create")
    public ResStatus create(@RequestBody AccountRequest accountRequest) {
        return accountService.create(accountRequest);
    }

    @PostMapping("/update")
    public ResStatus update(@RequestBody AccountRequest accountRequest) {
        return accountService.update(accountRequest);
    }

    @PostMapping("/forgotPassword")
    public VerifyCustomerResponse forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return accountService.forgotPassword(forgotPasswordRequest);
    }

    @PostMapping("/confirmOTP")
    public ResStatus confirmOTP(@RequestBody ConfirmCustomerRequest confirmRequest) {
        return accountService.confirmOTP(confirmRequest);
    }

    @PostMapping("/updatePassword")
    public ResStatus updatePassword(@RequestBody UpdatePasswordRequest request) {
        return accountService.updatePassword(request);
    }

}
