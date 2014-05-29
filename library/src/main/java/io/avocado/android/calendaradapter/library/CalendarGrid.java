package io.avocado.android.calendaradapter.library;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Date;

/**
 * Created by matthewlogan on 5/22/14.
 */

public class CalendarGrid extends LinearLayout {

    private Context mContext;

    private Typeface mTypeface;
    private int mTextColor;

    private int mEventColor;

    private int mGrayColor;
    private int mWhiteColor;
    private int mPastEventColor;

    public CalendarGrid(Context context) {
        this(context, null);
    }

    public CalendarGrid(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        setOrientation(VERTICAL);

        int rowHeight = (int) context.getResources().getDimension(R.dimen.cal_row_height);

        for (int i = 0; i < 6; i++) {
            LinearLayout calendarRow = new LinearLayout(mContext);

            LayoutParams rowLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rowHeight);
            calendarRow.setLayoutParams(rowLp);
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

        mGrayColor = getResources().getColor(R.color.gray);
        mWhiteColor = getResources().getColor(R.color.white);
        mPastEventColor = getResources().getColor(R.color.past_event_color);
    }

    public void initCalendar(Date someDateInMonth) {
        int daysInCurrentMonth = CalendarUtils.getNumberOfDaysInMonth(someDateInMonth);
        int daysInPreviousMonth = CalendarUtils.getNumberOfDaysInPreviousMonth(someDateInMonth);
        int daysToShowInPreviousMonthBeforeThisMonth
                = CalendarUtils.getNumberOfDaysToShowInPreviousMonthBeforeThisMonth(someDateInMonth);

        for (int calPosition = 0; calPosition < 42; calPosition++) {

            int rowNum = calPosition / 7;
            int colNum = calPosition - 7 * rowNum;

            LinearLayout row = (LinearLayout) getChildAt(rowNum);

            CalendarCell calendarCell = (CalendarCell) row.getChildAt(colNum);

            if (mTypeface != null) {
                calendarCell.setTypeface(mTypeface);
            }

            if (mTextColor != -1) {
                calendarCell.setTextColor(mTextColor);
            }

            int dayNum;
            int bgColor;
            int eventColor;

            if (calPosition < daysToShowInPreviousMonthBeforeThisMonth) {

                dayNum = daysInPreviousMonth - daysToShowInPreviousMonthBeforeThisMonth + calPosition + 1;
                bgColor = mGrayColor;
                eventColor = mPastEventColor;

            } else if (calPosition >= daysToShowInPreviousMonthBeforeThisMonth &&
                    calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth <= daysInCurrentMonth) {

                dayNum = calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth;
                bgColor = mWhiteColor;
                eventColor = mEventColor;

            } else {

                dayNum = calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth - daysInCurrentMonth;
                bgColor = mGrayColor;
                eventColor = mPastEventColor;
            }

            calendarCell.setDateText(String.valueOf(dayNum));
            calendarCell.setBackgroundColor(bgColor);
            calendarCell.setEventColor(eventColor);

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
}
