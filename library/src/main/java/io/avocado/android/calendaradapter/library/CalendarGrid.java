package io.avocado.android.calendaradapter.library;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by matthewlogan on 5/22/14.
 */

public class CalendarGrid extends LinearLayout implements View.OnClickListener {

    private Context mContext;

    private Typeface mTypeface;
    private int mTextColor;

    private int mEventColor;

    private Date mSomeDateInMonth;

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

                calendarRow.addView(calendarCell);
            }

            addView(calendarRow);
        }
    }

    public void initCalendar(Date someDateInMonth) {
        mSomeDateInMonth = someDateInMonth;
        int daysInCurrentMonth = CalendarUtils.getNumberOfDaysInMonth(someDateInMonth);
        int daysInPreviousMonth = CalendarUtils.getNumberOfDaysInPreviousMonth(someDateInMonth);
        int daysToShowInPreviousMonthBeforeThisMonth
                = CalendarUtils.getNumberOfDaysToShowInPreviousMonthBeforeThisMonth(someDateInMonth);

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

            if (mTextColor != -1) {
                calendarCell.setTextColor(mTextColor);
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
            calendarCell.setEventColor(mEventColor);
            calendarCell.setRelativeMonth(relativeMonth);
            calendarCell.setOnClickListener(this);

            if (dayNum % 3 == 0 && dayNum % 5 == 0) {
                calendarCell.setNumEvents(4);
            } else if (dayNum % 3 == 0) {
                calendarCell.setNumEvents(3);
            } else if (dayNum % 5 == 0) {
                calendarCell.setNumEvents(2);
            } else if (dayNum % 2 == 0) {
                calendarCell.setNumEvents(1);
            } else {
                calendarCell.setNumEvents(0);
            }
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
