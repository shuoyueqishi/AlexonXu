package com.xlt.idempotent;

import com.alexon.authorization.utils.RedisUtil;
import com.alexon.exception.IdempotentException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 幂等切面设置
 *
 * @author Alex
 */
@Aspect
@Component
@ConditionalOnClass(RedisTemplate.class)
public class IdempotentAspect {
    private static Logger logger = LoggerFactory.getLogger(IdempotentAspect.class);

    /**
     * 根据实际路径进行调整
     */
    @Pointcut("@annotation(com.xlt.idempotent.Idempotent)")
    public void executeIdempotent() {
    }

    @Around("executeIdempotent()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取幂等注解
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        //根据 key前缀 + @Idempotent.value() + 方法签名 + 参数 构建缓存键值
        //确保幂等处理的操作对象是：同样的 @Idempotent.value() + 方法签名 + 参数
        String key = String.format("idempotent_%s", idempotent.value() + "_" + KeyUtil.generate(method, joinPoint.getArgs()));
        //存入redis
        if (RedisUtil.hasKey(key)&&RedisUtil.get(key).equals(key)) {
            throw new IdempotentException("Repetitive request for "+key);
        }
        RedisUtil.set(key, key, idempotent.expireMillis());
        return joinPoint.proceed();
    }
}
