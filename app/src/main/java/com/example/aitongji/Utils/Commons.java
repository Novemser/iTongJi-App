package com.example.aitongji.Utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Novemser on 2016/11/21.
 */
public class Commons {

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static int getWeekOfDateDecimal(Date dt) {
        int[] weekDays = {6, 0, 1, 2, 3, 4, 5};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getWeekOfDate(int dt) {
        String[] weekDays = {"一", "二", "三", "四", "五", "六", "日"};
        if (dt < 0 || dt > 6)
            return "";
        return weekDays[dt];
    }

    public static String getStartTime(int num) {
        switch (num) {
            case 1:
                return "08:00";
            case 2:
                return "08:55";
            case 3:
                return "10:00";
            case 4:
                return "10:55";
            case 5:
                return "13:30";
            case 6:
                return "14:20";
            case 7:
                return "15:25";
            case 8:
                return "16:15";
            case 9:
                return "18:30";
            case 10:
                return "19:25";
            default:
                return "00:00";
        }
    }
}
