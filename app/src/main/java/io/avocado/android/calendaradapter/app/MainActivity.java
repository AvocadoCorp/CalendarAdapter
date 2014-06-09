package io.avocado.android.calendaradapter.app;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.avocado.android.calendaradapter.library.CalendarAdapter;
import io.avocado.android.calendaradapter.library.CalendarEvent;


public class MainActivity extends Activity implements CalendarAdapter.OnDateSelectedListener {

    private CalendarAdapter adapter;

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
        int todayBackgroundColor = getResources().getColor(R.color.today_background);

        List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, -1);
        while (cal.getTime().before(endDate)) {
            // Add a 4 day event
            if (cal.get(Calendar.DAY_OF_MONTH) == 30) {
                Calendar endCal = Calendar.getInstance();
                endCal.setTime(cal.getTime());
                endCal.add(Calendar.DAY_OF_MONTH, 7);
                Date eventEndTime = endCal.getTime();
                calendarEvents.add(new CalendarEvent(cal.getTime(), eventEndTime));
            }

            // Add a bunch of 1 day events
            if (cal.get(Calendar.DAY_OF_MONTH) % 5 == 0) {
                calendarEvents.add(new CalendarEvent(cal.getTime(), null));
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        adapter = new CalendarAdapter.Builder(this)
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
                .todayCalendarCellBackgroundColor(todayBackgroundColor)
                .onDateSelectedListener(this)
                .create();

        calendarListView.setAdapter(adapter);

        adapter.setCalendarEvents(calendarEvents);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDateSelected(Date date) {
        adapter.addCalendarEvent(new CalendarEvent(date, null));
        adapter.notifyDataSetChanged();
    }
}
