package util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * 日期工具类
 * 提供日期时间的格式化和解析功能
 */
public class DateUtils {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * 格式化日期
     * @param date 日期
     * @return 格式化后的字符串
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * 格式化时间
     * @param time 时间
     * @return 格式化后的字符串
     */
    public static String formatTime(LocalTime time) {
        if (time == null) return "";
        return time.format(TIME_FORMATTER);
    }
    
    /**
     * 解析日期字符串
     * @param dateStr 日期字符串（yyyy-MM-dd）
     * @return LocalDate对象，解析失败返回null
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * 解析时间字符串
     * @param timeStr 时间字符串（HH:mm）
     * @return LocalTime对象，解析失败返回null
     */
    public static LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) return null;
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * 获取本周的开始日期（周一）
     * @return 本周开始日期
     */
    public static LocalDate getWeekStart() {
        return LocalDate.now().with(WeekFields.of(Locale.CHINA).dayOfWeek(), 1);
    }
    
    /**
     * 获取本周的结束日期（周日）
     * @return 本周结束日期
     */
    public static LocalDate getWeekEnd() {
        return getWeekStart().plusDays(6);
    }
    
    /**
     * 获取本月的开始日期
     * @return 本月开始日期
     */
    public static LocalDate getMonthStart() {
        return LocalDate.now().withDayOfMonth(1);
    }
    
    /**
     * 获取本月的结束日期
     * @return 本月结束日期
     */
    public static LocalDate getMonthEnd() {
        return LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
    }
    
    /**
     * 获取指定日期所在周的开始日期
     * @param date 指定日期
     * @return 周开始日期
     */
    public static LocalDate getWeekStart(LocalDate date) {
        return date.with(WeekFields.of(Locale.CHINA).dayOfWeek(), 1);
    }
    
    /**
     * 获取指定日期所在周的结束日期
     * @param date 指定日期
     * @return 周结束日期
     */
    public static LocalDate getWeekEnd(LocalDate date) {
        return getWeekStart(date).plusDays(6);
    }
    
    /**
     * 计算两个日期之间的天数差
     * @param start 开始日期
     * @param end 结束日期
     * @return 天数差
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }
    
    /**
     * 判断日期是否在指定范围内
     * @param date 要判断的日期
     * @param start 开始日期
     * @param end 结束日期
     * @return 是否在范围内
     */
    public static boolean isDateInRange(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null || start == null || end == null) return false;
        return !date.isBefore(start) && !date.isAfter(end);
    }
    
    /**
     * 获取今天的日期字符串
     * @return 今天日期字符串
     */
    public static String getTodayString() {
        return formatDate(LocalDate.now());
    }
    
    /**
     * 获取当前时间字符串
     * @return 当前时间字符串
     */
    public static String getCurrentTimeString() {
        return formatTime(LocalTime.now());
    }
    
    /**
     * 获取日期的中文星期几
     * @param date 日期
     * @return 中文星期几
     */
    public static String getChineseDayOfWeek(LocalDate date) {
        if (date == null) return "";
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        return days[date.getDayOfWeek().getValue() - 1];
    }
}