package com.zj.everybodyvotes.utils;

import java.time.*;

/**
 * 时间处理工具类
 * @author cuberxp
 * @since 1.0.0
 * Create time 2019/12/16 13:52
 */
public class DateUtil {
    /**
     * 将localDateTime转换成时间戳
     * localDateTime：单位秒
     * @param localDateTime 待转换的日期时间类
     * @return 从1970- 01-01T00:00:00Z 开始的秒数 时区UTC +8
     */
    public static long localDateTimeToStampTime(LocalDateTime localDateTime){
        return localDateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 将时间戳转换为日期时间类型
     * @param stampTime long类型的时间戳，单位秒
     * @return 转换成的{@link LocalDateTime},时区默认
     */
    public static LocalDateTime stampTimeToLocalDateTime(Long stampTime){
        Instant instant = Instant.ofEpochMilli(stampTime * 1000);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 将时间戳转换为日期类型
     * @param stampTime long类型的时间戳，单位秒
     * @return 日期
     */
    public static LocalDate stampTimeToLocalDate(Long stampTime) {
        return stampTimeToLocalDateTime(stampTime).toLocalDate();
    }

    /**
     * 将LocalDate转换成时间戳
     * @param localDate 待转换的日期类
     * @return 从1970-01-01T00:00:00Z 开始的秒数 时区UTC +8
     */
    public static long localDateToStampTime(LocalDate localDate) {
        return localDateTimeToStampTime(localDate.atTime(0,0,0));
    }
}
