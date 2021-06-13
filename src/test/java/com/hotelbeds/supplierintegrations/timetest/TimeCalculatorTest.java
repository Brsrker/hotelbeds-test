package com.hotelbeds.supplierintegrations.timetest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeCalculatorTest {

    @Test
    public void testMinutesApart_whenLocalTimeAndPlus5Minutes_thenLong() {
        ZonedDateTime time1 = ZonedDateTime.now();
        ZonedDateTime time2 = ZonedDateTime.now().plusMinutes(5);
        long minutesApart = TimeCalculator.minutesApart(time1, time2);
        assertEquals(5, minutesApart);
    }

    @Test
    public void testMinutesApart_whenLocalTimeAndPlus5MinutesPlus59Seconds_thenLong() {
        ZonedDateTime time1 = ZonedDateTime.now();
        ZonedDateTime time2 = ZonedDateTime.now().plusMinutes(5).plusSeconds(59);
        long minutesApart = TimeCalculator.minutesApart(time1, time2);
        assertEquals(5, minutesApart);
    }

    @Test
    public void testMinutesApart_whenLocalTimeAndPlus5MinutesMinus59Seconds_thenLong() {
        ZonedDateTime time1 = ZonedDateTime.now();
        ZonedDateTime time2 = ZonedDateTime.now().plusMinutes(5).minusSeconds(59);
        long minutesApart = TimeCalculator.minutesApart(time1, time2);
        assertEquals(4, minutesApart);
    }

    @Test
    public void testParseTimestamp_whenTimestampIsValid_thenZonedDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2000, 12, 21, 16, 1, 7);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.of("+02:00"));
        String timestamp = "Thu, 21 Dec 2000 16:01:07 +0200";
        ZonedDateTime timeParsed = TimeCalculator.parseTimestamp(timestamp);
        assertEquals(timeParsed, zonedDateTime);
    }

    @Test
    public void testMinutesApart_whenTimestampString_thenLong() {
        ZonedDateTime time1 = LocalDateTime.of(2000, 12, 21, 16, 1, 7)
                .atZone(ZoneOffset.of("+02:00"));
        ZonedDateTime time2 = LocalDateTime.of(2000, 12, 21, 16, 5, 50)
                .atZone(ZoneOffset.of("+02:00"));
        String time1String = DateTimeFormatter.RFC_1123_DATE_TIME.format(time1);
        String time2String = DateTimeFormatter.RFC_1123_DATE_TIME.format(time2);
        long minutesApart = TimeCalculator.minutesApart(time1String, time2String);
        assertEquals(4, minutesApart);
    }

}
