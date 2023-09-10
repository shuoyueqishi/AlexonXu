package com.xlt.utils;

import com.alexon.exception.CommonException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtil {

    public static final String DATE_PATTERN_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_2 = "yyyy-MM-dd";

    public static Date parseDate(String dateStr, String pattern) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            log.error("String date parse error:", e);
            throw new CommonException(e.getMessage());
        }
        return date;
    }

    public static  Date parseDate(String dateStr) {
       return parseDate(dateStr, DATE_PATTERN_1);
    }

    public static  String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_1);
        return sdf.format(date);
    }

    public static  int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }
}
