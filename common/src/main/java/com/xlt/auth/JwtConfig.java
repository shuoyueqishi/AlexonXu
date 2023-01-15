package com.xlt.auth;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@RefreshScope
public class JwtConfig {

   @Value("${jwt.token.secret:12345abcd}")
    private String tokenSecret;

    @Value("${jwt.expiration:1800}")
    private long jwtExpireTime;

    @Value("${jwt.skip.permission:false}")
    private boolean skipPermission;
}
