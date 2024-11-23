package com.example.quay_so.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.quay_so.configuration.TokenConfig;
import com.example.quay_so.model.entity.AccountsEntity;
import com.example.quay_so.model.entity.UsersEntity;
import com.example.quay_so.token.DataToken;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtToken {
    public String generateToken(TokenConfig tokenConfig, Object user, String customerType) {
        Algorithm algorithm = Algorithm.HMAC256(tokenConfig.getSecret());
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        long expiredDate = System.currentTimeMillis() + Long.parseLong(tokenConfig.getExpriedTime());
        String token = "";
        if (user instanceof UsersEntity) {
            UsersEntity usersEntity = (UsersEntity) user;
            token = JWT.create()
                    .withHeader(header)
                    .withExpiresAt(new Date(expiredDate))
                    .withClaim(Contants.USER_ID, usersEntity.getId())
                    .withClaim(Contants.USER_NAME, usersEntity.getUserName())
                    .withClaim(Contants.PHONE_NUMBER, usersEntity.getPhoneNumber())
                    .withClaim(Contants.CUSTOMER_TYPE, customerType)
                    .sign(algorithm);
        } else if (user instanceof AccountsEntity) {
            AccountsEntity accountsEntity = (AccountsEntity) user;
            token = JWT.create()
                    .withHeader(header)
                    .withExpiresAt(new Date(expiredDate))
                    .withClaim(Contants.USER_ID, accountsEntity.getId())
                    .withClaim(Contants.USER_NAME, accountsEntity.getUserName())
                    .withClaim(Contants.PHONE_NUMBER, accountsEntity.getPhoneNumber())
                    .withClaim(Contants.CUSTOMER_TYPE, customerType)
                    .sign(algorithm);
        }
        return DataToken.encrypt(token, tokenConfig.getSecret());
    }
}
