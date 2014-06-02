package io.avocado.android.calendaradapter.app;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import io.avocado.android.calendaradapter.library.CalendarAdapter;


public class MainActivity extends ActionBarActivity implements CalendarAdapter.OnDateSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView calendarListView = (ListView) findViewById(R.id.calendar_list);

        Date startDate = new Date();

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.MONTH, 3);
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

        Set<Date> eventDates = new TreeSet<Date>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        while (cal.getTime().before(endDate)) {
            if (cal.get(Calendar.DAY_OF_MONTH) % 5 == 0) {
                eventDates.add(cal.getTime());
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        CalendarAdapter adapter = new CalendarAdapter.Builder(this)
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
                .eventDates(eventDates)
                .onDateSelectedListener(this)
                .create();

        calendarListView.setAdapter(adapter);
    }

    @Override
    public void onDateSelected(Date date) {
        Log.d("testing", "On date selected: " + date);
    }
}
