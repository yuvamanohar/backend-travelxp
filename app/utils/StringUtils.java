package utils;

import java.util.Locale;
import java.util.Random;

/**
 * Created by yuva on 19/4/17.
 */
public class StringUtils {

    public static Integer MOD_USER_NAME = 10000 ;
    public static Random random = new Random() ;

    public static String toLowerCase(String src) {
        return src.toLowerCase(Locale.ENGLISH) ;
    }
    public static String toUpperCase(String src) {
        return src.toUpperCase(Locale.ENGLISH) ;
    }

    public static String generateUserName(String userNameFeed) {
        return toLowerCase(userNameFeed).replaceAll("[^A-Za-z0-9]", "") ;
    }

    public static String generateRandomUserName(String userNameFeed) {
        random.setSeed(System.currentTimeMillis()) ;
        return generateUserName(userNameFeed) +  Math.abs(random.nextInt() % MOD_USER_NAME) ;
    }
}
