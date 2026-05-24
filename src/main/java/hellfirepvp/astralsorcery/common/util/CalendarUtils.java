package hellfirepvp.astralsorcery.common.util;

import java.time.Month;
import java.time.LocalDateTime;

public class CalendarUtils
{
    public static boolean isAprilFirst() {
        final LocalDateTime date = LocalDateTime.now();
        return date.getMonth() == Month.APRIL && date.getDayOfMonth() == 1;
    }
}
