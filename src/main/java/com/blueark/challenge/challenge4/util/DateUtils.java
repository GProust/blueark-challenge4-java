package com.blueark.challenge.challenge4.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static Integer getMonth(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return gregorianCalendar.get(Calendar.MONTH);
    }

    public static Integer getYear(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return gregorianCalendar.get(Calendar.YEAR);
    }

    public static Date changeDate(int years, int months, int days, Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.add(Calendar.YEAR, years);
        gregorianCalendar.add(Calendar.MONTH, months);
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, days);
        return gregorianCalendar.getTime();
    }
}
