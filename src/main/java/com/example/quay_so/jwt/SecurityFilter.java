package com.example.quay_so.jwt;

import com.example.quay_so.configuration.TokenConfig;
import com.example.quay_so.exception.ErrorCode;
import com.example.quay_so.exception.ProjectException;
import com.example.quay_so.model.dto.UserInfoDto;
import com.example.quay_so.utils.validate.ConvertUtils;
import com.example.quay_so.validator.TokenValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityFilter implements Filter {
    private final TokenValidator tokenValidator;
    private final TokenConfig tokenConfig;

    public SecurityFilter(TokenValidator tokenValidator, TokenConfig tokenConfig) {
        this.tokenValidator = tokenValidator;
        this.tokenConfig = tokenConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            String token = getToken(req.getHeader("Authorization"));
//            String sessionId = req.getHeader("sessionId");
            if (StringUtils.isBlank(token)) {
                throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            UserInfoDto userInfoDto = tokenValidator.verifyToken(token, tokenConfig.getSecret());
            authenticateUser(userInfoDto);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new ProjectException(ErrorCode.INTERNAL_SERVER_ERROR, "Token không hợp lệ");
        }
    }

    private String getToken(String tokenRq) {
        String token = null;
        String[] tokens = tokenRq.split("\\s+");

        // Kiểm tra xem có đủ phần tử không
        if (tokens.length == 2 && tokens[0].equalsIgnoreCase("Bearer")) {
            // Lấy token từ phần tử thứ 1
            token = tokens[1];
        }
        return token;
    }

    public void authenticateUser(UserInfoDto userInfoDto) {
        String userJson = ConvertUtils.toJson(userInfoDto);
        // Tạo một đối tượng Authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(userJson, null, null);

        // Lưu Authentication vào SecurityContext
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }
}
