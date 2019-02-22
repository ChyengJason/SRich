package com.jscheng.srich.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class DateUtil {
    public static boolean isSameDate(long time1, long time2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(time1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(time2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    public static String formatDate(long time) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日");
        return fmt.format(new Date(time));
    }
}
