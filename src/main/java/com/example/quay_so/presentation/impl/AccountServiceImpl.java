package com.example.quay_so.presentation.impl;

import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.jwt.PasswordService;
import com.example.quay_so.model.dto.UserInfoDto;
import com.example.quay_so.model.entity.AccountsEntity;
import com.example.quay_so.model.entity.RequestCustomerEntity;
import com.example.quay_so.model.repository.AccountRepository;
import com.example.quay_so.model.repository.RequestCustomerRepository;
import com.example.quay_so.model.request.accounts.AccountRequest;
import com.example.quay_so.model.request.accounts.ForgotPasswordRequest;
import com.example.quay_so.model.request.accounts.UpdatePasswordRequest;
import com.example.quay_so.model.request.transaction.ConfirmCustomerRequest;
import com.example.quay_so.model.request.transaction.VerifyCustomerResponse;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.accounts.AccountResponse;
import com.example.quay_so.presentation.service.AccountService;
import com.example.quay_so.utils.*;
import com.example.quay_so.utils.mapper.AccountMapper;
import com.example.quay_so.utils.validate.AccountValidate;
import com.example.quay_so.utils.validate.ConvertUtils;
import com.example.quay_so.utils.validate.CustomerType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    AccountRepository accountRepository;

    @Resource
    AccountValidate accountValidate;

    @Resource
    RequestCustomerRepository requestCustomerRepository;

    @Override
    public AccountResponse info() {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.CUSTOMER.name());
        Optional<AccountsEntity> accountsEntity = accountRepository.findById(userInfoDto.getUserId());
        return accountsEntity.map(AccountMapper::mapAccountEntityToAccountResponse).orElseGet(AccountResponse::new);
    }

    @Override
    public ResStatus create(AccountRequest accountRequest) {
        processAccount(accountRequest, FunctionCode.CREATE_ACCOUNT);
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus update(AccountRequest accountRequest) {
        UserInfoDto userInfoDto = UserInfoService.getUserInfo(CustomerType.CUSTOMER.name());
        accountRequest.setId(userInfoDto.getUserId());
        processAccount(accountRequest, FunctionCode.UPDATE_ACCOUNT);
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public VerifyCustomerResponse forgotPassword(ForgotPasswordRequest request) {
        VerifyCustomerResponse response = new VerifyCustomerResponse();
        Optional<AccountsEntity> accountsEntity = accountRepository.findByPhoneNumber(request.getPhoneNumber());
        if (!accountsEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Số điện thoại không hợp lệ");
        }
        //send OTP
        String otpId = UUID.randomUUID().toString();
        String requestId = UUID.randomUUID().toString();
        RequestCustomerEntity entity = new RequestCustomerEntity();
        entity.setId(requestId);
        entity.setUserId(accountsEntity.get().getId());
        entity.setRequestType(RequestType.FORGOT_PASSWORD.name());
        entity.setStatus(TransactionStatus.DRAF.name());
        entity.setCreatedBy(accountsEntity.get().getUserName());
        entity.setOtpId(otpId);
        entity.setCreatedAt(new Date(System.currentTimeMillis()));
        requestCustomerRepository.save(entity);
        response.setRequestId(requestId);
        response.setOtpId(otpId);
        response.setPhoneNumber(UserInfoService.maskPhoneNumber(accountsEntity.get().getPhoneNumber()));
        return response;
    }

    @Override
    public ResStatus confirmOTP(ConfirmCustomerRequest request) {
        Optional<RequestCustomerEntity> entity = requestCustomerRepository.findByIdAndStatus(request.getRequestId(), TransactionStatus.DRAF.name());
        if (!entity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        if (!StringUtils.equalsIgnoreCase(request.getOtpId(), entity.get().getOtpId())) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        if (!StringUtils.equalsIgnoreCase(request.getOtp(), Contants.OTP)) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Mã OTP không đúng");
        }
        entity.get().setStatus(TransactionStatus.INIT.name());
        requestCustomerRepository.save(entity.get());
        return new ResStatus(Contants.SUCCESS);
    }

    @Override
    public ResStatus updatePassword(UpdatePasswordRequest request) {
        if (StringUtils.isBlank(request.getPasswordNew()) || StringUtils.isBlank(request.getRequestId())) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        Optional<RequestCustomerEntity> entity = requestCustomerRepository.findByIdAndStatus(request.getRequestId(), TransactionStatus.INIT.name());
        if (!entity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        entity.get().setRequestBody(ConvertUtils.toJson(request));
        Optional<AccountsEntity> accountsEntity = accountRepository.findById(entity.get().getUserId());
        if (!accountsEntity.isPresent()) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }
        String passWordNew = PasswordService.encodePassword(request.getPasswordNew());
        accountsEntity.get().setPassword(passWordNew);
        entity.get().setStatus(TransactionStatus.SUCCESS.name());
        accountRepository.save(accountsEntity.get());
        requestCustomerRepository.save(entity.get());
        return new ResStatus(Contants.SUCCESS);
    }

    private void processAccount(AccountRequest rqBody, FunctionCode functionCode) {
        accountValidate.validateRequestAccount(rqBody, functionCode);
        AccountsEntity accountsEntity = new AccountsEntity();
        switch (functionCode) {
            case CREATE_ACCOUNT:
                accountsEntity.setId(UUID.randomUUID().toString());
                accountsEntity.setUserName(rqBody.getUserName());
                accountsEntity.setFullName(rqBody.getFullName());
                accountsEntity.setEmail(rqBody.getEmail());
                accountsEntity.setPhoneNumber(rqBody.getPhoneNumber());
                accountsEntity.setAddress(rqBody.getAddress());
                accountsEntity.setStatus(StatusType.ACTIVE.name());
                String password = PasswordService.encodePassword(StringUtils.isBlank(rqBody.getPassword()) ? Contants.PASSWORD_DEFAULT : rqBody.getPassword());
                accountsEntity.setPassword(password);
                accountsEntity.setBirthday(DateUtils.convertStringToDate(rqBody.getBirthday()));
                accountsEntity.setCreatedAt(new Date(System.currentTimeMillis()));
                accountsEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                break;
            case UPDATE_ACCOUNT:
                Optional<AccountsEntity> entity = accountRepository.findById(rqBody.getId());
                if (!entity.isPresent()) {
                    throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
                } else {
                    accountsEntity = entity.get();
                    accountsEntity.setFullName(rqBody.getFullName());
                    accountsEntity.setEmail(rqBody.getEmail());
                    accountsEntity.setPhoneNumber(rqBody.getPhoneNumber());
                    accountsEntity.setAddress(rqBody.getAddress());
                    accountsEntity.setStatus(rqBody.getStatus());
                    accountsEntity.setBirthday(DateUtils.convertStringToDate(rqBody.getBirthday()));
                    accountsEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                }
                break;
        }
        accountRepository.save(accountsEntity);
    }
}
