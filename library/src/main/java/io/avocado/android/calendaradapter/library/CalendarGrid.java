package io.avocado.android.calendaradapter.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by matthewlogan on 5/22/14.
 */

public class CalendarGrid extends LinearLayout {

    private Context mContext;

    public CalendarGrid(Context context) {
        this(context, null);
    }

    public CalendarGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("testing", "in view constructor");

        mContext = context;

        setOrientation(VERTICAL);

        for (int i = 0; i < 6; i++) {
            LinearLayout calendarRow = new LinearLayout(mContext);

            LayoutParams rowLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            calendarRow.setLayoutParams(rowLp);

            calendarRow.setOrientation(HORIZONTAL);

            for (int j = 0; j < 7; j++) {
                TextView calendarCell = new TextView(mContext);

                LayoutParams cellLp = new LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                cellLp.weight = 1;
                calendarCell.setLayoutParams(cellLp);

                calendarRow.addView(calendarCell);
            }

            addView(calendarRow);
        }
    }

    public void initCalendar(Date someDateInMonth) {
        int daysInCurrentMonth = CalendarUtils.getNumberOfDaysInMonth(someDateInMonth);
        int daysInPreviousMonth = CalendarUtils.getNumberOfDaysInPreviousMonth(someDateInMonth);
        int daysToShowInPreviousMonthBeforeThisMonth
                = CalendarUtils.getNumberOfDaysToShowInPreviousMonthBeforeThisMonth(someDateInMonth);


        int grayColor = getResources().getColor(R.color.gray);
        int whiteColor = getResources().getColor(R.color.white);

        boolean inNextMonth = false;

        for (int calPosition = 0; calPosition < 42; calPosition++) {

            int rowNum = calPosition / 7;
            int colNum = calPosition - 7 * rowNum;

            LinearLayout row = (LinearLayout) getChildAt(rowNum);

            TextView calendarCell = (TextView) row.getChildAt(colNum);

            int dayNum;

            if (calPosition < daysToShowInPreviousMonthBeforeThisMonth) {

                dayNum = daysInPreviousMonth - daysToShowInPreviousMonthBeforeThisMonth + calPosition + 1;
                calendarCell.setBackgroundColor(grayColor);

            } else if (calPosition >= daysToShowInPreviousMonthBeforeThisMonth &&
                    calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth <= daysInCurrentMonth) {

                dayNum = calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth;
                calendarCell.setBackgroundColor(whiteColor);

            } else {

                dayNum = calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth - daysInCurrentMonth;
                calendarCell.setBackgroundColor(grayColor);
                inNextMonth = true;

            }

            if (inNextMonth && colNum == 0) {
                row.setVisibility(View.GONE);
                break;
            } else {
                row.setVisibility(View.VISIBLE);
            }

            calendarCell.setText(String.valueOf(dayNum));
        }
    }
}
