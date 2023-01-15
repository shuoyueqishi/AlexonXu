package com.xlt.config.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@RefreshScope
public class JwtConfig {

    @Value("${jwt.token.secret}")
    private String tokenSecret;

    @Value("${jwt.expiration}")
    private long jwtExpireTime;
}
