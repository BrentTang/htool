package com.vimdream.htool.time;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * @Title: DateUtil
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/9/18 10:09
 */
public class DateUtil {

    private DateUtil(){}

    public static int getAge(Date birthday) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");
        String cur = sdf.format(now);
        String birth = sdf.format(birthday);
        int age = Integer.valueOf(Integer.valueOf(cur.substring(0, 4))- Integer.valueOf(birth.substring(0,4)));
        return cur.compareTo(birth) < 0 ? --age : age;
    }

    public static Date plus(long amountToAdd, TemporalUnit unit) {
        return plus(null, amountToAdd, unit);
    }

    public static Date plus(LocalDateTime start, long amountToAdd, TemporalUnit unit) {
        LocalDateTime time = start;
        if (time == null) {
            time = LocalDateTime.now();
        }
        return localDateTime2Date(time.plus(amountToAdd, unit));
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date localDate2Date(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static LocalDate date2LocalDate(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

}
