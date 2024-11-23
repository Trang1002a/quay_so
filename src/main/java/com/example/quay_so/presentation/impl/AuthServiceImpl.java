package com.example.quay_so.presentation.impl;

import com.example.quay_so.configuration.TokenConfig;
import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.jwt.PasswordService;
import com.example.quay_so.model.entity.AccountsEntity;
import com.example.quay_so.model.entity.UsersEntity;
import com.example.quay_so.model.repository.AccountRepository;
import com.example.quay_so.model.repository.UserRepository;
import com.example.quay_so.model.request.auth.LoginRequest;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.auth.LoginResponse;
import com.example.quay_so.presentation.service.AuthService;
import com.example.quay_so.utils.JwtToken;
import com.example.quay_so.utils.StatusType;
import com.example.quay_so.utils.validate.CustomerType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    UserRepository userRepository;

    @Resource
    AccountRepository accountRepository;

    @Resource
    JwtToken jwtToken;

    @Resource
    TokenConfig tokenConfig;

    @Override
    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        String userName = request.getUserName();
        String password = request.getPassword();
        String customerType = request.getCustomerType();
        if (StringUtils.isBlank(userName)) {
            throw new ProjectException(ErrorCode.USERNAME_CANNOT_BE_EMPTY);
        }
        if (StringUtils.isBlank(password)) {
            throw new ProjectException(ErrorCode.PASSWORD_CANNOT_BE_EMPTY);
        }
        String token = "";
        String userNameRes = "";
        String fullName = "";
        if (StringUtils.equalsIgnoreCase(customerType, CustomerType.ADMIN.name())) {
            Optional<UsersEntity> userEntity = userRepository.findByUserNameAndStatus(
                    userName, StatusType.ACTIVE.name());
            if (!userEntity.isPresent()) {
                throw new ProjectException(ErrorCode.INCORRECT_ACCOUNT_OR_PASSWORD);
            }
            if (!PasswordService.matchPassword(password, userEntity.get().getPassword())) {
                throw new ProjectException(ErrorCode.INCORRECT_ACCOUNT_OR_PASSWORD);
            }
            userNameRes = userEntity.get().getUserName();
            fullName = userEntity.get().getFullName();
            token = jwtToken.generateToken(tokenConfig, userEntity.get(), customerType);
        } else if (StringUtils.equalsIgnoreCase(customerType, CustomerType.CUSTOMER.name())) {
            Optional<AccountsEntity> accountsEntity = accountRepository.findByUserNameAndStatus(
                    userName, StatusType.ACTIVE.name());
            if (!accountsEntity.isPresent()) {
                throw new ProjectException(ErrorCode.INCORRECT_ACCOUNT_OR_PASSWORD);
            }
            if (!PasswordService.matchPassword(password, accountsEntity.get().getPassword())) {
                throw new ProjectException(ErrorCode.INCORRECT_ACCOUNT_OR_PASSWORD);
            }
            userNameRes = accountsEntity.get().getUserName();
            fullName = accountsEntity.get().getFullName();
            token = jwtToken.generateToken(tokenConfig, accountsEntity.get(), customerType);
        } else {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        response.setUserName(userNameRes);
        response.setFullName(fullName);
        response.setToken(token);
        response.setExpiredTime(new Date((System.currentTimeMillis() + Long.parseLong(tokenConfig.getExpriedTime()))).toString());
        return response;
    }

    @Override
    public ResStatus logout() {
        SecurityContextHolder.clearContext();
        return new ResStatus();
    }
}
