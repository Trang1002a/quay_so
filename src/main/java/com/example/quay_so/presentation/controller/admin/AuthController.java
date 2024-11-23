package com.example.quay_so.presentation.controller.admin;

import com.example.quay_so.model.request.auth.LoginRequest;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.model.response.auth.LoginResponse;
import com.example.quay_so.presentation.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResStatus logout(HttpServletRequest httpRequest) {
        return authService.logout();
    }
}
