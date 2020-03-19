package Logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingLibrary {
    public static String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]: ";
        return time;
    }
}
