package utils;

import java.util.Locale;

/**
 * Created by yuva on 19/4/17.
 */
public class StringUtils {

    public static String toLowerCase(String src) {
        return src.toLowerCase(Locale.ENGLISH) ;
    }
    public static String toUpperCase(String src) {
        return src.toUpperCase(Locale.ENGLISH) ;
    }
}
