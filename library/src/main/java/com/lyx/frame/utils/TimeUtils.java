package com.lyx.frame.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TimeUtils
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class TimeUtils {

    public static String longToDatetime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(new Date(Long.valueOf(time)));
    }

    public static String longToDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(new Date(Long.valueOf(time)));
    }

    public static String longToMonthDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd", Locale.getDefault());
        return format.format(new Date(Long.valueOf(time)));
    }

    public static String longToTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return format.format(new Date(Long.valueOf(time)));
    }

    public static String longToHourMinute(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return format.format(new Date(Long.valueOf(time)));
    }

    public static String currentDateTime() {
        return longToDatetime(System.currentTimeMillis());
    }

    public static String currentDate() {
        return longToDate(System.currentTimeMillis());
    }

    public static String currentTime() {
        return longToTime(System.currentTimeMillis());
    }

    public static String currentHourMinute() {
        return longToHourMinute(System.currentTimeMillis());
    }

    public static String clipDatetimeMS(String datetimeWithMS) {
        if (datetimeWithMS.endsWith(".0")) {
            return datetimeWithMS.substring(0, datetimeWithMS.length() - 2);
        }
        return datetimeWithMS;
    }

    public static long datetimeToLong(String time) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        long longTime = 0;
        try {
            longTime = dateformat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return longTime;
    }

    public static boolean isInScope(long startTime, long endTime, long currentTime) {
        return startTime < currentTime && currentTime < endTime;
    }

    public static boolean isInScope(String hourMinute1, String hourMinute2, long currentTime) {
        long time1 = datetimeToLong(longToDate(currentTime) + " " + hourMinute1 + ":00");
        long time2 = datetimeToLong(longToDate(currentTime) + " " + hourMinute2 + ":00");
        return isInScope(time1, time2, currentTime);
    }

    public static String showTime(String timeStr) {
        return showTime(datetimeToLong(timeStr));
    }

    public static String showTime(long time) {
        long time2 = System.currentTimeMillis();
        String strTime = "1970-01-01";
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(new Date(time));
        c2.setTime(new Date(time2));
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int mouth1 = c1.get(Calendar.MONTH);
        int day = c1.get(Calendar.DAY_OF_MONTH);
        int day1 = c1.get(Calendar.DAY_OF_YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);
        int hour1 = c1.get(Calendar.HOUR_OF_DAY);
        int hour2 = c2.get(Calendar.HOUR_OF_DAY);
        int minute1 = c1.get(Calendar.MINUTE);
        int minute2 = c2.get(Calendar.MINUTE);
        String hourMin = (hour1 < 10 ? "0" + hour1 : hour1) + ":" + (minute1 < 10 ? "0" + minute1 : minute1);
        String mouthDay = (mouth1 < 9 ? "0" + (mouth1 + 1) : (mouth1 + 1)) + "-" + (day < 10 ? "0" + day : day);
        if (time2 - time < 60 * 1000) strTime = "刚刚";
        else if (time2 - time < 30 * 60 * 1000) {
            if (hour2 == hour1) strTime = (minute2 - minute1) + " 分钟前";
            else strTime = (minute2 + 60 - minute1) + " 分钟前";
        } else if (year1 == year2 && day1 == day2) strTime = "今天 " + hourMin;
        else if (year1 == year2 && day2 - day1 == 1) strTime = "昨天 " + hourMin;
        else if (year1 == year2 && day2 - day1 == 2) strTime = "前天 " + hourMin;
        else if (year1 == year2 && day2 - day1 > 2) strTime = mouthDay + " " + hourMin;
        else if (year1 != year2) strTime = year1 + "-" + mouthDay;
        if (strTime.startsWith("-")) strTime = strTime.substring(1, strTime.length());
        return strTime;
    }

}
