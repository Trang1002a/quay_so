package com.example.quay_so.utils.validate;

import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.model.entity.AccountsEntity;
import com.example.quay_so.model.repository.AccountRepository;
import com.example.quay_so.model.request.accounts.AccountRequest;
import com.example.quay_so.utils.FunctionCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
public class AccountValidate {
    @Autowired
    private AccountRepository accountRepository;

    public void validateRequestAccount(AccountRequest request, FunctionCode functionCode) {
        switch (functionCode) {
            case CREATE_ACCOUNT:
                if (StringUtils.isBlank(request.getUserName())
                        || StringUtils.isBlank(request.getPhoneNumber())
                        || StringUtils.isBlank(request.getEmail())
                        || StringUtils.isBlank(request.getFullName())
                        || StringUtils.isBlank(request.getStatus())
                        || StringUtils.isBlank(request.getAddress())
                        || StringUtils.isBlank(request.getPassword())
                        || StringUtils.isBlank(request.getBirthday())) {
                    throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
                break;
            case UPDATE_ACCOUNT:
                if (StringUtils.isBlank(request.getPhoneNumber())
                        || StringUtils.isBlank(request.getEmail())
                        || StringUtils.isBlank(request.getFullName())
                        || StringUtils.isBlank(request.getStatus())
                        || StringUtils.isBlank(request.getAddress())
                        || StringUtils.isBlank(request.getBirthday())) {
                    throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
                break;
            default:
                throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Thông tin request không hợp lệ");
        }

        if (checkExistUserName(request.getUserName(), request.getId())) {
            throw new ProjectException("", "UserName đã tồn tại");
        }
        if (checkExistPhoneNumber(request.getPhoneNumber(), request.getId())) {
            throw new ProjectException("", "Tồn tại người dùng có cùng số điện thoại");
        }
        if (checkExistEmail(request.getEmail(), request.getId())) {
            throw new ProjectException("", "Tồn tại người dùng có cùng Email");
        }
        if (!isValidDateOfBirth(request.getBirthday())) {
            throw new ProjectException("", "Ngày sinh không hợp lệ");
        }

    }

    public boolean checkExistUserName(String userName, String id) {
        Optional<AccountsEntity> accountsEntity = accountRepository.findByUserName(userName);
        return accountsEntity.isPresent() && checkId(accountsEntity.get().getId(), id);
    }

    public boolean checkExistPhoneNumber(String phoneNumber, String id) {
        Optional<AccountsEntity> userEntity = accountRepository.findByPhoneNumber(phoneNumber);
        return userEntity.isPresent() && checkId(userEntity.get().getId(), id);
    }

    public boolean checkExistEmail(String email, String id) {
        Optional<AccountsEntity> userEntity = accountRepository.findByEmail(email);
        return userEntity.isPresent() && checkId(userEntity.get().getId(), id);
    }

    private boolean checkId(String id, String requestId) {
        return StringUtils.isBlank(requestId) || !StringUtils.equalsIgnoreCase(id, requestId);
    }

    public boolean isValidDateOfBirth(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            Date dateOfBirth = dateFormat.parse(dateString);
            Date currentDate = new Date();
            return dateOfBirth.before(currentDate);
        } catch (Exception e) {
            return false;
        }
    }
}
