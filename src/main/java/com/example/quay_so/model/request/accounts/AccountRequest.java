package com.example.quay_so.model.request.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    private String id;
    private String userName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;
    private String status;
    private String birthday;
}
