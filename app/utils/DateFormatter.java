package utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yuva on 17/4/17.
 */
public class DateFormatter {
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss") ;

    public static String getReadableCurrentTime() {
        Date now = Calendar.getInstance().getTime();

        return formatter.format(now) ;
    }

    public static String getTimeBeforeXdays(int x) {
        Calendar calendar = Calendar.getInstance() ;
        Date now = calendar.getTime();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, -x);
        return formatter.format(calendar.getTime()) ;
    }

}
