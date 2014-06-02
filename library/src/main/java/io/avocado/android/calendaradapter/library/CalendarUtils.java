package io.avocado.android.calendaradapter.library;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by matthewlogan on 5/21/14.
 */
public class CalendarUtils {

    public static int getNumberOfMonthsApartInclusive(Date firstDate, Date lastDate) {
        Calendar firstCal = Calendar.getInstance();
        firstCal.setTime(firstDate);

        Calendar lastCal = Calendar.getInstance();
        lastCal.setTime(lastDate);

        return 12 * (lastCal.get(Calendar.YEAR) - firstCal.get(Calendar.YEAR))
                + lastCal.get(Calendar.MONTH) - firstCal.get(Calendar.MONTH) + 1;
    }

    public static int getNumberOfDaysToShowInPreviousMonthBeforeThisMonth(Date someDayInCurrentMonth) {
        Calendar monthCal = Calendar.getInstance();
        monthCal.setTime(someDayInCurrentMonth);
        monthCal.set(Calendar.DAY_OF_MONTH, 1);
        switch (monthCal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return 0;
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
            default:
                return 0;
        }
    }

    public static int getNumberOfDaysInMonth(Date someDayInMonth) {
        Calendar monthCal = Calendar.getInstance();
        monthCal.setTime(someDayInMonth);
        return monthCal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getNumberOfDaysInPreviousMonth(Date someDayInCurrentMonth) {
        Calendar lastMonthCal = Calendar.getInstance();
        lastMonthCal.setTime(someDayInCurrentMonth);
        lastMonthCal.add(Calendar.MONTH, -1);
        return lastMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static boolean isSameMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }
}
