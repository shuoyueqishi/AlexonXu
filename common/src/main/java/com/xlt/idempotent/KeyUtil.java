package com.xlt.idempotent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;

/**
 * Key生成工具
 *
 * @author Alex
 */
public class KeyUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyUtil.class);

    /**
     * 根据{方法名 + 参数列表}和md5转换生成key
     */
    public static String generate(Method method, Object... args) {
        StringBuilder sb = new StringBuilder(method.toString());
        for (Object arg : args) {
            sb.append(toString(arg));
        }
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
    }

    private static String toString(Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof Number) {
            return object.toString();
        }
        //调用json工具类转换成String
        return JsonUtil.toJson(object);
    }
}

/**
 * Json格式化工具
 *
 * @author Alex
 */
class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * Java Object Maps To Json
     */
    public static String toJson(Object obj) {
        String result;
        if (obj == null || obj instanceof String) {
            return (String) obj;
        }
        try {
            result = MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            LOGGER.error("Java Object Maps To Json Error !");
            throw new RuntimeException("Java Object Maps To Json Error !", e);
        }
        return result;
    }
}

