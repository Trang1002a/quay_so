package com.example.quay_so.model.request.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmCustomerRequest {
    private String requestId;
    private String otpId;
    private String otp;
}