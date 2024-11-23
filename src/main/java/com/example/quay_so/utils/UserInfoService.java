package com.example.quay_so.utils;

import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.model.dto.UserInfoDto;
import com.example.quay_so.utils.validate.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserInfoService {
    public static UserInfoDto getUserInfo(String customerType) {
        UserInfoDto userInfoDto = new UserInfoDto();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String tokenJson = authentication.getName();
            userInfoDto = ConvertUtils.fromJson(tokenJson, UserInfoDto.class);
            if (StringUtils.equalsIgnoreCase(userInfoDto.getCustomerType(), customerType)) {
                return userInfoDto;
            }
        }
        throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public static String maskPhoneNumber(String phoneNumber) {
        // Kiểm tra xem số điện thoại có ít nhất 6 ký tự hay không
        if (phoneNumber != null && phoneNumber.length() >= 6) {
            // Lấy 6 ký tự đầu tiên
            String prefix = phoneNumber.substring(0, 6);

            // Tạo chuỗi mask với số '*' có độ dài bằng với prefix
            StringBuilder maskBuilder = new StringBuilder();
            for (int i = 0; i < prefix.length(); i++) {
                maskBuilder.append("*");
            }
            String mask = maskBuilder.toString();

            // Thay thế 6 số đầu bằng chuỗi mask
            return phoneNumber.replaceFirst(prefix, mask);
        } else {
            // Trả về số điện thoại không thay đổi nếu không đủ ký tự
            return phoneNumber;
        }
    }
}
