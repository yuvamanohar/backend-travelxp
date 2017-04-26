package utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yuva on 17/4/17.
 */
public class DateFormatter {

    public static String getReadableCurrentTime() {
        Date now = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss") ;
        return formatter.format(now) ;
    }

}
