package com.example.quay_so.model.response.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String id;
    private String roleId;
    private String userName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;
    private String status;
    private Date createdAt;
    private Date updatedAt;
}
