package com.xlt.filter;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.xlt.utils.JwtUtil;
import com.xlt.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static List<String> whitePathList = new ArrayList<String>() {
        private static final long serialVersionUID = -2860067364426320457L;

        {
            add("/user/login");
            add("/user/logout");
        }
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getPath().toString();
        log.info("path={}", path);

        // 忽略以下url请求
        if (whitePathList.contains(path)) {
            return chain.filter(exchange);
        }

        // 安全起见，去掉内部鉴权的请求头internal
        ServerHttpRequest.Builder reqBuilder = request.mutate();
        String internal = request.getHeaders().getFirst("internal");
        if (StringUtils.isNotEmpty(internal)) {
            log.info("remove illegal request header:inernal");
            reqBuilder.headers(k -> k.remove("internal"));
        }
        String token = request.getHeaders().getFirst("token");
        if (StringUtils.isBlank(token)) {
            log.info("token is empty");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        } else {
            // 验证token
            try {
                log.info("token={}", token);
                Map<String, Claim> claimMap = JwtUtil.verifyToken(token);
                log.info("claimMap={}", JSON.toJSONString(claimMap));
                Long userId = claimMap.get("userId").asLong();
                Map<Object, Object> userInfoCached = redisTemplate.opsForHash().entries("UserInfo:" + userId);
                if (CollectionUtils.isEmpty(userInfoCached)) {
                    throw new TokenExpiredException("token expired");
                }
                reqBuilder.header("userId", String.valueOf(userId));
                log.info("token is valid");
            } catch (Exception e) {
                log.error("token error:", e);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }
        ServerHttpRequest httpRequest = reqBuilder.build();
        ServerWebExchange webExchange = exchange.mutate().request(httpRequest).build();
        return chain.filter(webExchange);
    }
}
