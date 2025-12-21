package com.student.career.zBase.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DateUtil {

    public static final String DATE_FORMAT_ENG = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_WITHOUT_SECOND = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_WITHOUT_TIME = "yyyy-MM-dd";


    //    public static LocalDateTime stringEnToDateTime(String strDate) {
//        if (StringUtil.isEmpty(strDate)) return null;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_ENG, Locale.ENGLISH);
//        return LocalDateTime.parse(strDate, formatter);
//    }
    public static final String DATE_FORMAT_ALTERNATIVE = "yyyy-MM-dd HH:mm";

    public static LocalDateTime stringEnToDateTime(String strDate) {
        if (StringUtil.isEmpty(strDate)) return null;

        List<String> dateFormats = Arrays.asList(DATE_FORMAT_ENG, DATE_FORMAT_ALTERNATIVE);
        for (String format : dateFormats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH);
                LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
                ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneOffset.UTC);
                return zonedDateTime.toLocalDateTime();
            } catch (DateTimeParseException e) {
                // Ignore and try the next format
            }
        }
        // Handle case where none of the formats match
        throw new DateTimeParseException("Unable to parse date: " + strDate, strDate, 0);
    }


    public static LocalDate stringEnToDate(String strDate) {
        if (StringUtil.isEmpty(strDate)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_ENG, Locale.ENGLISH);
        return LocalDate.parse(strDate, formatter);
    }

    public static String dateTimeToString(LocalDateTime date) {
        if (isNull(date)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_WITHOUT_SECOND, Locale.ENGLISH);
        OffsetDateTime off = OffsetDateTime.of(date, ZoneOffset.UTC);
        ZonedDateTime zonedDateTime = off.atZoneSameInstant(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime().format(formatter);
    }

    public static String dateTimeToStringDateWithoutTime(LocalDateTime date) {
        if (isNull(date)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITHOUT_TIME, Locale.ENGLISH);
        OffsetDateTime off = OffsetDateTime.of(LocalDateTime.from(date), ZoneOffset.UTC);
        ZonedDateTime zonedDateTime = off.atZoneSameInstant(ZoneId.of("UTC+1"));
        return zonedDateTime.toLocalDateTime().format(formatter);
    }

    public static LocalDateTime toZonedDateTime(LocalDateTime date) {
        if (isNull(date)) return null;
        ZonedDateTime zdt = date.atZone(ZoneOffset.UTC);
        return zdt.withZoneSameInstant(ZoneOffset.systemDefault()).toLocalDateTime();

    }


    public static boolean isNull(LocalDateTime date) {
        return date == null;
    }

    public static boolean isNull(LocalDate date) {
        return date == null;
    }

    public static boolean isNotNull(LocalDateTime date) {
        return !isNull(date);
    }

    public static boolean isNotNull(LocalDate date) {
        return !isNull(date);
    }


}
