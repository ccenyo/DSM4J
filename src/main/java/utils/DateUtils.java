package utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {

    private DateUtils(){}

    public static Long convertLocalDateTimeToUnixTimestamp(LocalDateTime time) {
        Instant instant = time.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }
}
