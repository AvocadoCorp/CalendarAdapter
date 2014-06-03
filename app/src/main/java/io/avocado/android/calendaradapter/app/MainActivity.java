package io.avocado.android.calendaradapter.app;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.avocado.android.calendaradapter.library.CalendarAdapter;


public class MainActivity extends ActionBarActivity implements CalendarAdapter.OnDateSelectedListener {

    private CalendarAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView calendarListView = (ListView) findViewById(R.id.calendar_list);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.MONTH, -1);
        Date startDate = startCalendar.getTime();

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.MONTH, 3);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = endCalendar.getTime();

        Typeface titleTypeface = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Thin.ttf");

        Typeface daysOfWeekTypeface = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light.ttf");

        Typeface calendarCellTypeface = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light.ttf");

        int mainTextColor = getResources().getColor(R.color.main_text);
        int eventColor = getResources().getColor(R.color.event_color);
        int pastFutureCalendarCellBackgroundColor = getResources().getColor(R.color.past_future_background);
        int pastFutureCalendarCellTextColor = getResources().getColor(R.color.past_future_text);
        int pastFutureEventColor = getResources().getColor(R.color.past_future_event);
        int calendarCellBorderColor = getResources().getColor(R.color.calendar_cell_border);

        List<Date> eventDates = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, -1);
        while (cal.getTime().before(endDate)) {
            if (cal.get(Calendar.DAY_OF_MONTH) % 5 == 0) {
                eventDates.add(cal.getTime());
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        mAdapter = new CalendarAdapter.Builder(this)
                .titleTypeface(titleTypeface)
                .titleTextColor(mainTextColor)
                .daysOfWeekTypeface(daysOfWeekTypeface)
                .daysOfWeekTextColor(mainTextColor)
                .calendarCellTypeface(calendarCellTypeface)
                .calendarCellTextColor(mainTextColor)
                .startDate(startDate)
                .endDate(endDate)
                .eventColor(eventColor)
                .pastFutureCalendarCellBackgroundColor(pastFutureCalendarCellBackgroundColor)
                .pastFutureCalendarCellTextColor(pastFutureCalendarCellTextColor)
                .pastFutureEventColor(pastFutureEventColor)
                .calendarCellBorderColor(calendarCellBorderColor)
                .eventDates(eventDates)
                .onDateSelectedListener(this)
                .create();

        calendarListView.setAdapter(mAdapter);
    }

    @Override
    public void onDateSelected(Date date) {
        mAdapter.addEventDate(date);
        mAdapter.notifyDataSetChanged();
    }
}
