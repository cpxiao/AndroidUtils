package com.cpxiao.androidutils.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * TimeUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DATE_FORMAT_DEFAULT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * cannot be instantiated
     **/
    private TimeUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * long time to string
     *
     * @param timeInMillis timeInMillis
     * @param dateFormat   dateFormat
     * @return String
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DATE_FORMAT_DEFAULT}
     *
     * @param timeInMillis timeInMillis
     * @return String
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DEFAULT);
    }

    /**
     * get current time in milliseconds
     *
     * @return long
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DATE_FORMAT_DEFAULT}
     *
     * @return String
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return String
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
}