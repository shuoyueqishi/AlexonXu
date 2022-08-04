package com.xlt.utils.common;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xlt.config.auth.JwtConfig;
import com.xlt.model.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
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
     * 生成用户token,设置token超时时间
     */
    public static String createToken(UserInfoVo userInfoVo) {
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + JwtUtil.jwtConfig.getJwtExpireTime() * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)// 添加头部
                //可以将基本信息放到claims中
                .withClaim("userId", userInfoVo.getCurUser().getUserId())//userId
                .withClaim("userName", userInfoVo.getCurUser().getName())//userName
                .withClaim("curRole", JSON.toJSONString(userInfoVo.getCurRole())) //curRole
                .withClaim("validRoleList", JSON.toJSONString(userInfoVo.getValidRoleList()))
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(new Date()) //签发时间
                .sign(Algorithm.HMAC256(JwtUtil.jwtConfig.getTokenSecret())); //SECRET加密
        return token;
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

