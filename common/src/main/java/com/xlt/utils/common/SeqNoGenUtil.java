package com.xlt.utils.common;

import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.utils.AppContextUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class SeqNoGenUtil {
    private static RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void setStaticParam() {
        SeqNoGenUtil.redisTemplate = (RedisTemplate<String, Object>) AppContextUtil.getBean("redisTemplate");
    }

    /**
     * 获取分布式全局唯一序列号
     *
     * @param prefix    前缀
     * @param seqLength 序列长度
     * @return 序列号
     */
    public static String getSequenceNo(String prefix, int seqLength) {
        AssertUtil.isTrue(seqLength <= 0, "seqLength must bigger than 0");
        RedisAtomicLong counter = new RedisAtomicLong(prefix, redisTemplate.getConnectionFactory());
        long currentValue = counter.getAndIncrement();
        int length = getNumberLength(currentValue);
        AssertUtil.isTrue(length > seqLength, "current sequence length is longer than seqLength, " +
                "please set seqLength bigger");
        StringBuilder retSb = new StringBuilder(prefix);
        if (length < seqLength) {
            for (int i = 0; i < seqLength - length; i++) {
                retSb.append("0");
            }
        }
        retSb.append(currentValue);
        return retSb.toString();
    }


    /**
     * 获取全局唯一序列号，形如：D20220723xxxx
     *
     * @param prefix    前缀
     * @param seqLength 序列长度，即xxxx长度
     * @return 序列号
     */
    public static String getSeqNoWithTime(String prefix, int seqLength) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(new Date());
        return getSequenceNo(prefix + date, seqLength);
    }

    private static int getNumberLength(long num) {
        int len = 0;
        long copyNum = num;
        while (copyNum > 0) {
            copyNum /= 10;
            len++;
        }
        return len;
    }

}
