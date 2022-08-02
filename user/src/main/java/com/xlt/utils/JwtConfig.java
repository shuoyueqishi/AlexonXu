package com.xlt.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JwtConfig {

    @Value("${jwt.token.secret}")
    private String tokenSecret;

    @Value("${jwt.expiration}")
    private long jwtExpireTime;
}
