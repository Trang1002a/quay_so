package com.example.quay_so.configuration;

import com.example.quay_so.jwt.SecurityFilter;
import com.example.quay_so.validator.TokenValidator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class SecurityConfig {
    @Resource
    private TokenValidator tokenValidator;

    @Resource
    private TokenConfig tokenConfig;

    @Bean
    public FilterRegistrationBean<SecurityFilter> filterFilterRegistrationBean() {
        FilterRegistrationBean<SecurityFilter> securityFilter =
                new FilterRegistrationBean<>();
        securityFilter.setFilter(new SecurityFilter(tokenValidator, tokenConfig));
        securityFilter.addUrlPatterns("/admin/*");
        securityFilter.addUrlPatterns("/customer/rate/*");
        securityFilter.addUrlPatterns("/customer/account/info");
        securityFilter.addUrlPatterns("/customer/account/update");
        securityFilter.addUrlPatterns("/customer/order/*");
        return securityFilter;
    }
}
