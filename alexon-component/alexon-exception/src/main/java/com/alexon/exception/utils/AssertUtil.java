package com.alexon.exception.utils;


import com.alexon.exception.CommonException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Objects;

public class AssertUtil {

    public static void isNull(Object obj,String msg) {
        if(Objects.isNull(obj)) {
            throw new CommonException(msg);
        }
    }

    public static void isNonNull(Object obj,String msg) {
        if(!Objects.isNull(obj)) {
            throw new CommonException(msg);
        }
    }

    public static void isStringEmpty(String str,String msg) {
        if(StringUtils.isEmpty(str)) {
            throw new CommonException(msg);
        }
    }

    public static void isTrue(boolean condition,String msg) {
        if(condition) {
            throw new CommonException(msg);
        }
    }

    public static void isNotTrue(boolean condition,String msg) {
        if(!condition) {
            throw new CommonException(msg);
        }
    }

    public static void isCollectionEmpty(Collection<?> collection,String msg) {
        if(CollectionUtils.isEmpty(collection)) {
            throw new CommonException(msg);
        }
    }

}
