package com.alexon.authorization.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtConfig {

   @Value("${jwt.token.secret:12345abcd}")
    private String tokenSecret;

    @Value("${jwt.expiration:1800}")
    private long jwtExpireTime;

    @Value("${jwt.skip.permission:false}")
    private boolean skipPermission;
}
