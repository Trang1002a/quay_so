package com.example.quay_so.model.request.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRequest {
    private List<String> requestId;
    private String otpId;
    private String otp;
}
