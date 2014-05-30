package io.avocado.android.calendaradapter.app;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;

import io.avocado.android.calendaradapter.library.CalendarAdapter;


public class MainActivity extends ActionBarActivity implements CalendarAdapter.OnDateSelectedListener {

    private ListView mCalendarListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalendarListView = (ListView) findViewById(R.id.calendar_list);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.MONTH, 15);

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

        CalendarAdapter adapter = new CalendarAdapter.Builder(this)
                .titleTypeface(titleTypeface)
                .titleTextColor(mainTextColor)
                .daysOfWeekTypeface(daysOfWeekTypeface)
                .daysOfWeekTextColor(mainTextColor)
                .calendarCellTypeface(calendarCellTypeface)
                .calendarCellTextColor(mainTextColor)
                .startDate(new Date())
                .endDate(endCalendar.getTime())
                .eventColor(eventColor)
                .pastFutureCalendarCellBackgroundColor(pastFutureCalendarCellBackgroundColor)
                .pastFutureCalendarCellTextColor(pastFutureCalendarCellTextColor)
                .pastFutureEventColor(pastFutureEventColor)
                .onDateSelectedListener(this)
                .daysOfWeekStrings(new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"})
                .create();

        mCalendarListView.setAdapter(adapter);
    }

    @Override
    public void onDateSelected(Date date) {
        Log.d("testing", "On date selected: " + date);
    }
}
