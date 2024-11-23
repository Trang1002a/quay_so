package com.example.quay_so.utils.validate;

import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.model.entity.UsersEntity;
import com.example.quay_so.model.repository.UserRepository;
import com.example.quay_so.model.request.accounts.UserRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidate {
    @Autowired
    private UserRepository userRepository;

    public void validateRequestUser(UserRequest userRequest) {
        if (StringUtils.isBlank(userRequest.getUserName())
                || StringUtils.isBlank(userRequest.getPhoneNumber())
                || StringUtils.isBlank(userRequest.getEmail())) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        if (checkExistUserName(userRequest.getUserName(), userRequest.getId())) {
            throw new ProjectException("", "UserName đã tồn tại");
        }
        if (checkExistPhoneNumber(userRequest.getPhoneNumber(), userRequest.getId())) {
            throw new ProjectException("", "Tồn tại người dùng có cùng số điện thoại");
        }
        if (checkExistEmail(userRequest.getEmail(), userRequest.getId())) {
            throw new ProjectException("", "Tồn tại người dùng có cùng Email");
        }
        if (StringUtils.isBlank(userRequest.getRoleId())) {
            throw new ProjectException("", "Thông tin request không hợp lệ");
        }

    }

    public boolean checkExistUserName(String userName, String id) {
        Optional<UsersEntity> userEntity = userRepository.findByUserName(userName);
        return userEntity.isPresent() && checkId(userEntity.get().getId(), id);
    }

    public boolean checkExistPhoneNumber(String phoneNumber, String id) {
        Optional<UsersEntity> userEntity = userRepository.findByPhoneNumber(phoneNumber);
        return userEntity.isPresent() && checkId(userEntity.get().getId(), id);
    }

    public boolean checkExistEmail(String email, String id) {
        Optional<UsersEntity> userEntity = userRepository.findByEmail(email);
        return userEntity.isPresent() && checkId(userEntity.get().getId(), id);
    }

    private boolean checkId(String id, String requestId) {
        return StringUtils.isBlank(requestId) || !StringUtils.equalsIgnoreCase(id, requestId);
    }
}
