package com.example.quay_so.presentation.service;

import com.example.quay_so.model.request.auth.LoginRequest;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    ResStatus logout();
}
