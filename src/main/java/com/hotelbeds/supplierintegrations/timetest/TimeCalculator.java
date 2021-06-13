package com.hotelbeds.supplierintegrations.timetest;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeCalculator {

    public static long minutesApart(String time1, String time2) {
        return minutesApart(parseTimestamp(time1), parseTimestamp(time2));
    }

    public static long minutesApart(ZonedDateTime time1, ZonedDateTime time2) {
        return ChronoUnit.MINUTES.between(time1, time2);
    }

    public static ZonedDateTime parseTimestamp(String timestamp) {
        return ZonedDateTime.parse(timestamp, DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
