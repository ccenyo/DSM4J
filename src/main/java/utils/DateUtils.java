package utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final String dsmDateFormat = "YYYY-MM-dd hh:mm:ss";

    private DateUtils(){}

    public static Long convertLocalDateTimeToUnixTimestamp(LocalDateTime time) {
        Instant instant = time.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    public static String convertDateToString(LocalDateTime localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(dsmDateFormat));
    }
}
