package com.alexon.distributed.id;

import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.utils.AppContextUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@Component
public class SeqNoGenUtil {
    private static RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void setStaticParam() {
        SeqNoGenUtil.redisTemplate = (RedisTemplate<String, Object>) AppContextUtil.getBean("redisTemplate");
    }

    /**
     * 获取分布式全局唯一序列号，默认缓存保存7天
     *
     * @param prefix    前缀
     * @param seqLength 序列长度
     * @return 分布式序列号
     */
    public static String getSequenceNo(String prefix, int seqLength) {
        return getSequenceNo(prefix, seqLength, Duration.ofDays(7));
    }

    /**
     * 获取分布式全局唯一序列号
     *
     * @param prefix    前缀
     * @param seqLength 序列长度
     * @param duration  缓存时间
     * @return 分布式序列号
     */
    public static String getSequenceNo(String prefix, int seqLength, Duration duration) {
        AssertUtil.isTrue(seqLength <= 0, "seqLength must bigger than 0");
        RedisAtomicLong counter = new RedisAtomicLong(prefix, redisTemplate.getConnectionFactory());
        counter.expire(duration);
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
     * @param prefix      前缀
     * @param timePattern 时间格式，如：yyyyMMdd, yyyyMMddHHmmss
     * @param seqLength   序列长度，即xxxx长度
     * @param duration    缓存时间
     * @return 分布式序列号
     */
    public static String getSeqNoWithTime(String prefix, String timePattern, int seqLength, Duration duration) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(timePattern);
        String date = dateFormat.format(new Date());
        return getSequenceNo(prefix + date, seqLength, duration);
    }

    /**
     * 获取全局唯一序列号，形如：D20220723xxxx
     *
     * @param prefix      前缀
     * @param seqLength   序列长度，即xxxx长度
     * @return 分布式序列号
     */
    public static String getSeqNoWithTime(String prefix, int seqLength) {
        return getSeqNoWithTime(prefix , "yyyyMMdd",seqLength, Duration.ofDays(7));
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
