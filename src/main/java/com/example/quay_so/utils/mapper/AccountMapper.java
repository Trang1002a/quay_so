package com.example.quay_so.utils.mapper;

import com.example.quay_so.model.entity.AccountsEntity;
import com.example.quay_so.model.response.accounts.AccountResponse;
import com.example.quay_so.utils.DateUtils;
import org.apache.commons.lang3.ObjectUtils;

public class AccountMapper {

    public static AccountResponse mapAccountEntityToAccountResponse(AccountsEntity entity) {
        AccountResponse response = new AccountResponse();
        response.setId(entity.getId());
        response.setUserName(entity.getUserName());
        response.setFullName(entity.getFullName());
        response.setEmail(entity.getEmail());
        response.setPhoneNumber(entity.getPhoneNumber());
        response.setAddress(entity.getAddress());
        response.setStatus(entity.getStatus());
        response.setBirthday(ObjectUtils.isEmpty(entity.getBirthday()) ? null: DateUtils.formatDate(entity.getBirthday()));
        response.setCreatedAt(entity.getCreatedAt().toString());
        return response;
    }
}
