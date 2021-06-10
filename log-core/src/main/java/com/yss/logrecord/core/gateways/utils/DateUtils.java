package com.yss.logrecord.core.gateways.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 时间工具类
 * </p>
 *
 * @author liuliangxing
 * @since 2020-02-11
 */
public class DateUtils {

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
       return date.toInstant()
               .atZone(ZoneId.systemDefault())
               .toLocalDateTime();
    }

    public static String formatDate(Date date) {
        return formatDate(toLocalDateTime(date));
    }

    public static String formatDate(LocalDateTime dateTime) {
       return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static LocalDateTime parseDate(String date,String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(date,dateTimeFormatter);
    }

    public static LocalDateTime parseDate(String date){
        return parseDate(date,DATE_FORMAT);
    }

    /**
     * 一天的开始
     * @return
     */
    public static LocalDateTime startOfTheDay(){
        LocalDateTime of = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return of;
    }

    public static String startOfTheDayString(){
        LocalDateTime startOfTheDay = startOfTheDay();
        return formatDate(startOfTheDay);
    }

    /**
     * 一天的开始
     * @param date
     * @return
     */
    public static LocalDateTime startOfTheDay(LocalDate date){
        return LocalDateTime.of(date, LocalTime.MIN);

    }

    /**
     * 当前时间
     * @return
     */
    public static String currentTime(){
        LocalDateTime now = LocalDateTime.now();
        return formatDate(now);
    }

    public static LocalDateTime currentDateTime(){
        return LocalDateTime.now();
    }

    /**
     * 本周的开始
     * @return
     */
    public static LocalDateTime startOfTheWeek(){
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        int value = dayOfWeek.getValue();
        now = now.minusDays(value - 1);
        return startOfTheDay(now);
    }

    public static String startOfTheWeekString(){
        return formatDate(startOfTheWeek());
    }

    /**
     * 本月的开始
     * @return
     */
    public static LocalDateTime startOfTheMouth(){
        LocalDate now = LocalDate.now();
        Month month = now.getMonth();
        now = LocalDate.of(now.getYear(),month,1);
        return startOfTheDay(now);
    }

    public static String startOfTheMouthString(){
        return formatDate(startOfTheMouth());
    }

    /**
     * 向前推进指定月份
     * @param mouth
     * @return
     */
    public static LocalDateTime pushForwardMouth(int mouth){
        LocalDate now = LocalDate.now();
        now = now.minusMonths(mouth);
        return startOfTheDay(now);
    }

    public static String pushForwardMouthString(int mouth){
        return formatDate(pushForwardMouth(mouth));
    }

    /**
     * 向前推进执行天数
     * @param day
     * @return
     */
    public static LocalDateTime pushForwardDay(int day){
        LocalDate now = LocalDate.now();
        now = now.minusDays(day);
        return startOfTheDay(now);
    }

    /**
     * 向后推进指定分钟时间
     * @param dateTime
     * @param minute
     * @return
     */
    public static LocalDateTime pushBackMinute(LocalDateTime dateTime,int minute){
        return dateTime.plusMinutes(minute);
    }


    /**
     * 判断是不是当前天
     * @param date
     * @return
     */
    public static boolean juageCurrentDate(LocalDate date){
        LocalDate now = LocalDate.now();
        return now.toString().equals(date.toString());
    }

    /**
     * 获取当前时间到凌晨的正点
     * @return
     */
    public static List<LocalDateTime> onPointTime(){
        LocalDateTime now = LocalDateTime.now();
        int second = now.getSecond();
        int minute = now.getMinute();
        int nano = now.getNano();
        now = now.minusSeconds(second);
        now = now.minusMinutes(minute);
        now = now.minusNanos(nano);
        LocalDateTime startOfTheDay = startOfTheDay();
        long between = ChronoUnit.HOURS.between(startOfTheDay, now);
        List<LocalDateTime> list = new ArrayList<>();
        list.add(startOfTheDay);
        for (int i = 0 ;i < between ; i++){
            LocalDateTime item = now.minusHours(i);
            list.add(item);
        }
        return list.stream().sorted().collect(Collectors.toList());
    }

    /**
     * 获取从开始时间到结束时间的分钟集合
     * @return
     */
    public static List<LocalDateTime> onPointTimeMinute(){
        List<LocalDateTime> result = new ArrayList<>();
        LocalDateTime start = startOfTheDay();
        while(start.isBefore(currentDateTime())){
            result.add(start);
            start = pushBackMinute(start,1);
        }
        return result;
    }

    public static List<LocalDateTime> onPointTimeMinute(String dateTime){
        List<LocalDateTime> result = new ArrayList<>();
        LocalDateTime start = startOfTheDay();
        while(start.isBefore(parseDate(dateTime))){
            result.add(start);
            start = pushBackMinute(start,1);
        }
        return result;
    }

}
