package com.example.quay_so.exception;

public interface ErrorCode {
    String ERROR_MESSAGE_DEFAULT_VN = "Đã có lỗi xảy ra trong quá trình kết nối. Quý khách vui lòng thử lại sau";
    String ERROR_MESSAGE_DEFAULT_EN = "An error occurred during the connection. Please try again later";
    String INTERNAL_SERVER_ERROR = "B-00-000";
    String USERNAME_CANNOT_BE_EMPTY = "B-00-001";
    String PASSWORD_CANNOT_BE_EMPTY = "B-00-002";
    String INCORRECT_ACCOUNT_OR_PASSWORD = "B-00-003";
}
