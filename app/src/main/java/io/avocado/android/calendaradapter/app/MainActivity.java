package io.avocado.android.calendaradapter.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;

import io.avocado.android.calendaradapter.library.CalendarAdapter;


public class MainActivity extends ActionBarActivity {

    private ListView mCalendarListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalendarListView = (ListView) findViewById(R.id.calendar_list);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.MONTH, 15);

        CalendarAdapter adapter = new CalendarAdapter.Builder(this)
                .startDate(new Date())
                .endDate(endCalendar.getTime())
                .create();

        mCalendarListView.setAdapter(adapter);
    }

}
