package com.xlt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xlt.model.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Slf4j
public class JwtUtil {
    /**
     * 过期时间,单位为秒
     **/
    private static final long EXPIRATION=1800;

    /**
     * 生成用户token,设置token超时时间
     */
    public static String createToken(UserInfoVo userInfoVo) {
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)// 添加头部
                //可以将基本信息放到claims中
                .withClaim("userId", userInfoVo.getUserId())//userId
                .withClaim("userName", userInfoVo.getName())//userName
                .withClaim("nickName", userInfoVo.getNickName())//name
                .withClaim("telephone", userInfoVo.getTelephone())
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(new Date()) //签发时间
                .sign(Algorithm.HMAC256(userInfoVo.getPassword())); //SECRET加密
        return token;
    }

    /**
     * 校验token
     */
    public Map<String, Claim> verifyToken(String token, String passWord) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(passWord)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            log.error("token解码异常", e);
            return null;
        }
        return jwt.getClaims();
    }
}

