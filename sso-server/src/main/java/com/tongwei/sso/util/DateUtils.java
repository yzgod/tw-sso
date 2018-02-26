package com.tongwei.sso.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author yangz
 * @date 2017年4月11日 下午3:56:03
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm", "yyyy.MM" };

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            if (date == null) {
                return null;
            }
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到年份字符串 格式（yyyy）
     */
    public static String getYear(Date date) {
        return formatDate(date, "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到月份字符串 格式（MM）
     */
    public static String getMonth(Date date) {
        return formatDate(date, "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到天字符串 格式（dd）
     */
    public static String getDay(Date date) {
        return formatDate(date, "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek(Date date) {
        return formatDate(date, "E");
    }

    /**
     * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
     * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取过去的天数
     * 
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     * 
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     * 
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     * 
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取两个日期之间的天数
     * 
     * @param before
     * @param after
     * @return
     */
    public static int getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (int) ((afterTime - beforeTime) / 86400000);
    }

    /**
     * HH:mm
     * 
     * @param beginTimeStr
     * @param endTimeStr
     */
    public static boolean isBelong(String beginTimeStr, String endTimeStr) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
        Date now = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse(beginTimeStr);
            endTime = df.parse(endTimeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return belongCalendar(now, beginTime, endTime);
    }

    /**
     * 判断时间是否在时间段内
     * 
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar t_24 = Calendar.getInstance();
        t_24.set(Calendar.HOUR_OF_DAY, 24);

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (begin.after(end) && (date.after(begin) || date.after(t_24) || date.before(end))) {
            return true;
        }
        if (begin.before(end) && (date.after(begin) && date.before(end))) {
            return true;
        }
        return false;
    }

    /**
     * 根据指定日期--获取指定指定的本周内的时间
     * <p>
     * 注意已经设置周一为本周的第一天
     * 
     * @param date
     *            --可以为字符串,也支持时间
     * @param CalendarInt
     * @return
     */
    public static Date getDayOfWeek(Object date, int CalendarInt) {
        return getDayOfWeek(date, CalendarInt, Calendar.MONDAY);
    }

    /**
     * 根据指定日期--获取指定指定的本周内的时间
     * 
     * @param date
     * @param CalendarInt
     * @return
     */
    public static Date getDayOfWeek(Object date, int CalendarInt, int firstDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        if (date instanceof Date) {
            calendar.setTime((Date) date);
        } else if (date instanceof String) {
            calendar.setTime(parseDate(date));
        } else {
            throw new IllegalArgumentException("date必须为字符串或者日期!");
        }
        calendar.set(Calendar.DAY_OF_WEEK, CalendarInt);
        return calendar.getTime();
    }

    /**
     * 获取当前日期--没有时分秒,默认00:00:00
     * 
     * @param date
     * @return
     */
    public static Date getDate(Date date) {
        return parseDate(formatDate(date));
    }

    /**
     * 判断一个日期是否是周几
     * 
     * @param dateStr
     * @param CalendarInt
     *            周几
     * @return
     */
    public static boolean dayOfWeek(int CalendarInt, String dateStr) {
        Date parseDate = parseDate(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate);
        if (calendar.get(Calendar.DAY_OF_WEEK) == CalendarInt) {
            return true;
        }
        return false;
    }

}
