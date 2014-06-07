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

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static int getNumberOfDaysInApartExclusive(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        int endYear = endCal.get(Calendar.YEAR);
        int startYear = startCal.get(Calendar.YEAR);

        int endDayOfYear = endCal.get(Calendar.DAY_OF_YEAR);
        int startDayOfYear = startCal.get(Calendar.DAY_OF_YEAR);

        if (endYear == startYear) {
            return endDayOfYear - startDayOfYear;
        } else {
            int daysInFuture = 0;

            // days left in this year
            daysInFuture += startCal.getActualMaximum(Calendar.DAY_OF_YEAR) - startDayOfYear;

            // days from start of end year
            daysInFuture += endDayOfYear;

            // all the days in the in-between years
            for (int year = startYear + 1; year < endYear; year++) {
                Calendar yearCal = Calendar.getInstance();
                yearCal.set(year, Calendar.JANUARY, 1);
                daysInFuture += yearCal.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return daysInFuture;
        }
    }
}
