package io.avocado.android.calendaradapter.library;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by matthewlogan on 5/22/14.
 */

public class CalendarGrid extends LinearLayout implements View.OnClickListener {

    private Context mContext;

    private Typeface mTypeface;
    private int mTextColor;

    private int mEventColor;

    private Date mSomeDateInMonth;

    private int mPastFutureCalendarCellBackgroundColor;
    private int mPastFutureCalendarCellTextColor;
    private int mPastFutureEventColor;

    private int mCalendarCellBorderColor;

    public CalendarAdapter.OnDateSelectedListener mListener;

    public CalendarGrid(Context context) {
        this(context, null);
    }

    public CalendarGrid(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        setOrientation(VERTICAL);

        for (int i = 0; i < 6; i++) {
            LinearLayout calendarRow = new LinearLayout(mContext);
            calendarRow.setOrientation(HORIZONTAL);

            for (int j = 0; j < 7; j++) {
                CalendarCell calendarCell = new CalendarCell(mContext);

                LayoutParams cellLp = new LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                cellLp.weight = 1;
                calendarCell.setLayoutParams(cellLp);

                if (i != 0 && i != 5 && j != 0 && j != 6) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.INSIDE);
                } else if (i == 0 && j == 0) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.TOP_LEFT_CORNER);
                } else if (i == 0 && j == 6) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.TOP_RIGHT_CORNER);
                } else if (i == 5 && j == 0) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.BOTTOM_LEFT_CORNER);
                } else if (i == 5 && j == 6) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.BOTTOM_RIGHT_CORNER);
                } else if (i == 0) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.TOP_EDGE);
                } else if (j == 0) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.LEFT_EDGE);
                } else if (i == 5) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.BOTTOM_EDGE);
                } else if (j == 6) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.RIGHT_EDGE);
                }

                calendarRow.addView(calendarCell);
            }

            addView(calendarRow);
        }
    }

    public void initCalendar(Date someDateInMonth, List<Date> currentMonthEventDates,
                             List<Date> previousMonthEventDates, List<Date> nextMonthEventDates) {
        mSomeDateInMonth = someDateInMonth;
        int daysInCurrentMonth = CalendarUtils.getNumberOfDaysInMonth(someDateInMonth);
        int daysInPreviousMonth = CalendarUtils.getNumberOfDaysInPreviousMonth(someDateInMonth);
        int daysToShowInPreviousMonthBeforeThisMonth
                = CalendarUtils.getNumberOfDaysToShowInPreviousMonthBeforeThisMonth(someDateInMonth);

        Calendar cal = Calendar.getInstance();
        int[] eventsPerDay = new int[42];
        int calPos;

        if (currentMonthEventDates != null) {
            for (Date date : currentMonthEventDates) {
                cal.setTime(date);
                calPos = daysToShowInPreviousMonthBeforeThisMonth + cal.get(Calendar.DAY_OF_MONTH) - 1;
                eventsPerDay[calPos] += 1;
            }
        }

        if (previousMonthEventDates != null) {
            for (Date date : previousMonthEventDates) {
                cal.setTime(date);
                calPos = daysToShowInPreviousMonthBeforeThisMonth -
                        (daysInPreviousMonth - cal.get(Calendar.DAY_OF_MONTH)) - 1;
                if (calPos >= 0) {
                    eventsPerDay[calPos] += 1;
                }
            }
        }

        if (nextMonthEventDates != null) {
            for (Date date : nextMonthEventDates) {
                cal.setTime(date);
                calPos = daysToShowInPreviousMonthBeforeThisMonth + daysInCurrentMonth
                        + cal.get(Calendar.DAY_OF_MONTH) - 1;
                if (calPos < 42) {
                    eventsPerDay[calPos] += 1;
                }
            }
        }

        for (int calPosition = 0; calPosition < 42; calPosition++) {

            int rowNum = calPosition / 7;
            int colNum = calPosition - 7 * rowNum;

            LinearLayout row = (LinearLayout) getChildAt(rowNum);

            CalendarCell calendarCell = (CalendarCell) row.getChildAt(colNum);
            if (calendarCell == null) {
                continue;
            }

            if (mTypeface != null) {
                calendarCell.setTypeface(mTypeface);
            }

            int dayNum;
            CalendarCell.RelativeMonth relativeMonth;

            if (calPosition < daysToShowInPreviousMonthBeforeThisMonth) {

                dayNum = daysInPreviousMonth - daysToShowInPreviousMonthBeforeThisMonth + calPosition + 1;
                relativeMonth = CalendarCell.RelativeMonth.PREVIOUS;

            } else if (calPosition >= daysToShowInPreviousMonthBeforeThisMonth &&
                    calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth <= daysInCurrentMonth) {

                dayNum = calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth;
                relativeMonth = CalendarCell.RelativeMonth.CURRENT;

            } else {

                dayNum = calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth - daysInCurrentMonth;
                relativeMonth = CalendarCell.RelativeMonth.NEXT;
            }

            calendarCell.setDayOfMonth(dayNum);
            calendarCell.setTextColor(mTextColor);
            calendarCell.setEventColor(mEventColor);
            calendarCell.setPastFutureCalendarCellBackgroundColor(mPastFutureCalendarCellBackgroundColor);
            calendarCell.setPastFutureCalendarCellTextColor(mPastFutureCalendarCellTextColor);
            calendarCell.setPastFutureEventColor(mPastFutureEventColor);
            calendarCell.setBorderColor(mCalendarCellBorderColor);
            calendarCell.setRelativeMonth(relativeMonth);
            calendarCell.setOnClickListener(this);

            calendarCell.setNumEvents(eventsPerDay[calPosition]);
        }
    }

    public void setTypeface(Typeface typeface) {
        mTypeface = typeface;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setEventColor(int eventColor) {
        mEventColor = eventColor;
    }

    public void setOnDateSelectedListener(CalendarAdapter.OnDateSelectedListener listener) {
        mListener = listener;
    }

    public void setPastFutureCalendarCellBackgroundColor(int pastFutureCalendarCellBackgroundColor) {
        mPastFutureCalendarCellBackgroundColor = pastFutureCalendarCellBackgroundColor;
    }

    public void setPastFutureCalendarCellTextColor(int pastFutureCalendarCellTextColor) {
        mPastFutureCalendarCellTextColor = pastFutureCalendarCellTextColor;
    }

    public void setPastFutureEventColor(int pastFutureEventColor) {
        mPastFutureEventColor = pastFutureEventColor;
    }

    public void setCalendarCellBorderColor(int calendarCellBorderColor) {
        mCalendarCellBorderColor = calendarCellBorderColor;
    }

    @Override
    public void onClick(View v) {
        CalendarCell cell = (CalendarCell) v;

        Calendar cal = Calendar.getInstance();
        cal.setTime(mSomeDateInMonth);
        cal.set(Calendar.DAY_OF_MONTH, cell.getDayOfMonth());

        if (cell.getRelativeMonth() == CalendarCell.RelativeMonth.PREVIOUS) {
            cal.add(Calendar.MONTH, -1);
        } else if (cell.getRelativeMonth() == CalendarCell.RelativeMonth.NEXT) {
            cal.add(Calendar.MONTH, 1);
        }

        mListener.onDateSelected(cal.getTime());
    }
}
