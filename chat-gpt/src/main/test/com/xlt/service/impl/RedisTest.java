package com.xlt.service.impl;

import com.xlt.model.vo.AccessInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisString() {
        String key = "testKey";
        Integer value = 1000;
        redisTemplate.opsForValue().set(key, value);
        Integer cached = (Integer) redisTemplate.opsForValue().get(key);
        log.info("cached={}", cached);
        redisTemplate.opsForValue().increment(key,1);
        cached = (Integer) redisTemplate.opsForValue().get(key);
        log.info("cached={}", cached);
        redisTemplate.opsForValue().decrement(key,10);
        cached = (Integer) redisTemplate.opsForValue().get(key);
        log.info("cached={}", cached);

        AccessInfoVo accessInfoVo = AccessInfoVo.builder().userId(10009L).apiKey("78dad9df799").credit(10).build();
        redisTemplate.opsForValue().set(key, accessInfoVo);
        AccessInfoVo cachedObj = (AccessInfoVo)redisTemplate.opsForValue().get(key);
        log.info("cachedObj={}", cachedObj);
        redisTemplate.delete(key);
        AccessInfoVo cachedObj1 = (AccessInfoVo)redisTemplate.opsForValue().get(key);
        log.info("cachedObj1={}", cachedObj1);
    }
}
