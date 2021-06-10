package com.yss.logrecord.tool.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static LocalDateTime currentTime(){
        return LocalDateTime.now();
    }

    public static long between(LocalDateTime startTime,LocalDateTime endTime){
        return ChronoUnit.MILLIS.between(startTime, endTime);
    }
}
