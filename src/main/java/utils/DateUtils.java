package utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String DSM_DATE_FORMAT = "YYYY-MM-dd";

    private DateUtils(){}

    public static Long convertLocalDateTimeToUnixTimestamp(LocalDateTime time) {
        Instant instant = time.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    public static String convertDateToString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DSM_DATE_FORMAT));
    }
}
