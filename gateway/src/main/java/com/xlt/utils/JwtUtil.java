package com.xlt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xlt.config.jwt.JwtConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 */
@Slf4j
@Component
public class JwtUtil implements ApplicationRunner {

    private static JwtConfig jwtConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JwtUtil.jwtConfig = AppContextUtil.getBean(JwtConfig.class);
    }

    /**
     * 校验token
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JwtUtil.jwtConfig.getTokenSecret())).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            log.error("token解码异常", e);
            throw new JWTDecodeException("token parse error");
        }
        return jwt.getClaims();
    }
}

