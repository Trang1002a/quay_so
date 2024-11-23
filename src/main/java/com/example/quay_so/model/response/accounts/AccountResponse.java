package com.example.quay_so.model.response.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private String id;
    private String userName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String status;
    private String birthday;
    private String createdAt;
}
