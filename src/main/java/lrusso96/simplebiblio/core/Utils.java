package lrusso96.simplebiblio.core;

import java.time.*;

public class Utils {

    private Utils() {
    }

    public static LocalDate parseUTC(String date) {
        Instant instant = Instant.parse(date);
        return LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())).toLocalDate();
    }

    public static LocalDate parseYear(String date) {
        return LocalDate.of(Integer.parseInt(date), 1, 1);
    }

    public static String bytesToReadableSize(int bytes) {
        int unit = 1000;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = Character.toString(("kMGTPE").charAt(exp - 1));
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}