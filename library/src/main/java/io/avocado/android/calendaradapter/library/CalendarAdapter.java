package io.avocado.android.calendaradapter.library;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by matthewlogan on 5/21/14.
 */
public class CalendarAdapter extends BaseAdapter implements ListAdapter {

    private Context context;

    private Date[] months;

    private String[] daysOfWeekStrings;

    private Typeface titleTypeface;
    private Typeface daysOfWeekTypeface;
    private Typeface calendarCellTypeface;

    private int titleTextColor;
    private int daysOfWeekTextColor;
    private int calendarCellTextColor;

    private int eventColor;

    private int pastFutureCalendarCellBackgroundColor;
    private int pastFutureCalendarCellTextColor;
    private int pastFutureEventColor;

    private int calendarCellBorderColor;

    private List<List<CalendarEvent>> calendarEventsInEachMonth;

    private OnDateSelectedListener listener;

    private static SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
    private static SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE", Locale.getDefault());

    public interface OnDateSelectedListener {
        public void onDateSelected(Date date);
    }

    private CalendarAdapter(Context context, Date[] months, String[] daysOfWeekStrings,
                            Typeface titleTypeface, Typeface daysOfWeekTypeface,
                            Typeface calendarCellTypeface, int titleTextColor,
                            int daysOfWeekTextColor, int calendarCellTextColor, int eventColor,
                            int pastFutureCalendarCellBackgroundColor,
                            int pastFutureCalendarCellTextColor, int pastFutureEventColor,
                            int calendarCellBorderColor, List<List<CalendarEvent>> calendarEventsInEachMonth,
                            OnDateSelectedListener listener) {

        this.context = context;
        this.months = months;
        this.daysOfWeekStrings = daysOfWeekStrings;
        this.titleTypeface = titleTypeface;
        this.daysOfWeekTypeface = daysOfWeekTypeface;
        this.calendarCellTypeface = calendarCellTypeface;
        this.titleTextColor = titleTextColor;
        this.daysOfWeekTextColor = daysOfWeekTextColor;
        this.calendarCellTextColor = calendarCellTextColor;
        this.eventColor = eventColor;
        this.pastFutureCalendarCellBackgroundColor = pastFutureCalendarCellBackgroundColor;
        this.pastFutureCalendarCellTextColor = pastFutureCalendarCellTextColor;
        this.pastFutureEventColor = pastFutureEventColor;
        this.calendarCellBorderColor = calendarCellBorderColor;
        this.calendarEventsInEachMonth = calendarEventsInEachMonth;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return months.length;
    }

    @Override
    public Object getItem(int position) {
        return calendarEventsInEachMonth != null ? calendarEventsInEachMonth.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        public TextView titleView;
        public LinearLayout daysOfWeekView;
        public CalendarGrid calendarGrid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.calendar_page, parent, false);
            if (convertView == null) {
                return null;
            }

            ViewHolder vh = new ViewHolder();

            vh.titleView = (TextView) convertView.findViewById(R.id.month_title);
            vh.titleView.setTypeface(titleTypeface);
            vh.titleView.setTextColor(titleTextColor);
            vh.titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    (int) context.getResources().getDimension(R.dimen.title_text_size));

            vh.daysOfWeekView = (LinearLayout) convertView.findViewById(R.id.days_of_week);

            for (int i = 0; i < vh.daysOfWeekView.getChildCount(); i++) {
                TextView dayLabel = (TextView) vh.daysOfWeekView.getChildAt(i);
                if (dayLabel != null) {
                    dayLabel.setText(daysOfWeekStrings[i]);
                }
            }

            int dayOfWeekTextSize = (int) context.getResources().getDimension(R.dimen.day_of_week_text_size);

            for (int i = 0; i < vh.daysOfWeekView.getChildCount(); i++) {
                TextView dayLabel = (TextView) vh.daysOfWeekView.getChildAt(i);
                if (dayLabel != null) {
                    if (daysOfWeekTypeface != null) {
                        dayLabel.setTypeface(daysOfWeekTypeface);
                    }

                    if (daysOfWeekTextColor != -1) {
                        dayLabel.setTextColor(daysOfWeekTextColor);
                    }

                    dayLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, dayOfWeekTextSize);
                }
            }

            vh.calendarGrid = (CalendarGrid) convertView.findViewById(R.id.month_grid);
            vh.calendarGrid.setTypeface(calendarCellTypeface);
            vh.calendarGrid.setTextColor(calendarCellTextColor);
            vh.calendarGrid.setEventColor(eventColor);
            vh.calendarGrid.setPastFutureCalendarCellBackgroundColor(pastFutureCalendarCellBackgroundColor);
            vh.calendarGrid.setPastFutureCalendarCellTextColor(pastFutureCalendarCellTextColor);
            vh.calendarGrid.setPastFutureEventColor(pastFutureEventColor);
            vh.calendarGrid.setCalendarCellBorderColor(calendarCellBorderColor);
            vh.calendarGrid.setOnDateSelectedListener(listener);

            convertView.setTag(vh);
        }
        ViewHolder vh = (ViewHolder) convertView.getTag();

        String prettyMonth = monthFormat.format(months[position]);
        vh.titleView.setText(prettyMonth);

        List<CalendarEvent> currentMonthEventDates = null;
        if (calendarEventsInEachMonth.size() > position + 1) {
            currentMonthEventDates = calendarEventsInEachMonth.get(position + 1);
        }

        List<CalendarEvent> previousMonthEventDates = null;
        if (calendarEventsInEachMonth.size() > position) {
            previousMonthEventDates = calendarEventsInEachMonth.get(position);
        }

        List<CalendarEvent> nextMonthEventDates = null;
        if (calendarEventsInEachMonth.size() > position + 2) {
            nextMonthEventDates = calendarEventsInEachMonth.get(position + 2);
        }

        vh.calendarGrid.initCalendar(months[position], currentMonthEventDates,
                previousMonthEventDates, nextMonthEventDates);

        return convertView;
    }

    public void addCalendarEvent(CalendarEvent calendarEvent) {
        if (calendarEventsInEachMonth == null) {
            calendarEventsInEachMonth = new ArrayList<List<CalendarEvent>>();
        }

        Calendar monthCal = Calendar.getInstance();
        monthCal.setTime(months[0]);
        monthCal.add(Calendar.MONTH, -1);

        Calendar eventCal = Calendar.getInstance();
        eventCal.setTime(calendarEvent.startDate);

        for (int i = 0; i <= months.length + 1; i++) {
            if (calendarEventsInEachMonth.get(i) == null) {
                calendarEventsInEachMonth.add(new ArrayList<CalendarEvent>());
            }

            if (monthCal.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
                    monthCal.get(Calendar.MONTH) == eventCal.get(Calendar.MONTH)) {
                calendarEventsInEachMonth.get(i).add(calendarEvent);
            }

            monthCal.add(Calendar.MONTH, 1);
        }
    }

    public void setCalendarEvents(List<CalendarEvent> calendarEvents) {
        if (calendarEventsInEachMonth == null) {
            calendarEventsInEachMonth = new ArrayList<List<CalendarEvent>>();
        } else {
            calendarEventsInEachMonth.clear();
        }

        Calendar monthCal = Calendar.getInstance();
        monthCal.setTime(months[0]);
        monthCal.add(Calendar.MONTH, -1);

        Calendar eventCal = Calendar.getInstance();
        for (int i = 0; i <= months.length + 1; i++) {
            List<CalendarEvent> eventDatesInMonth = new ArrayList<CalendarEvent>();
            for (CalendarEvent calendarEvent : calendarEvents) {
                eventCal.setTime(calendarEvent.startDate);
                if (monthCal.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
                        monthCal.get(Calendar.MONTH) == eventCal.get(Calendar.MONTH)) {
                    eventDatesInMonth.add(calendarEvent);
                }
            }
            calendarEventsInEachMonth.add(eventDatesInMonth);
            monthCal.add(Calendar.MONTH, 1);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public static class Builder {

        private Context context;

        private Date startDate;
        private Date endDate;

        private String[] daysOfWeekStrings;

        private Typeface titleTypeface;
        private Typeface daysOfWeekTypeface;
        private Typeface calendarCellTypeface;

        private int titleTextColor = -1;
        private int daysOfWeekTextColor = -1;
        private int calendarCellTextColor = -1;

        private int eventColor = -1;

        private int pastFutureCalendarCellBackgroundColor = -1;
        private int pastFutureCalendarCellTextColor = -1;
        private int pastFutureEventColor = -1;

        private int calendarCellBorderColor = -1;

        private List<CalendarEvent> calendarEvents;

        private OnDateSelectedListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder startDate(Date someDateInStartMonth) {
            this.startDate = someDateInStartMonth;
            return this;
        }

        public Builder endDate(Date someDateInEndMonth) {
            this.endDate = someDateInEndMonth;
            return this;
        }

        public Builder titleTypeface(Typeface titleTypeface) {
            this.titleTypeface = titleTypeface;
            return this;
        }

        public Builder daysOfWeekTypeface(Typeface daysOfWeekTypeface) {
            this.daysOfWeekTypeface = daysOfWeekTypeface;
            return this;
        }

        public Builder calendarCellTypeface(Typeface calendarCellTypeface) {
            this.calendarCellTypeface = calendarCellTypeface;
            return this;
        }

        public Builder titleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder daysOfWeekTextColor(int daysOfWeekTextColor) {
            this.daysOfWeekTextColor = daysOfWeekTextColor;
            return this;
        }

        public Builder calendarCellTextColor(int calendarCellTextColor) {
            this.calendarCellTextColor = calendarCellTextColor;
            return this;
        }

        public Builder eventColor(int eventColor) {
            this.eventColor = eventColor;
            return this;
        }

        public Builder pastFutureCalendarCellBackgroundColor(int pastFutureCalendarCellBackgroundColor) {
            this.pastFutureCalendarCellBackgroundColor = pastFutureCalendarCellBackgroundColor;
            return this;
        }

        public Builder pastFutureCalendarCellTextColor(int pastFutureCalendarCellTextColor) {
            this.pastFutureCalendarCellTextColor = pastFutureCalendarCellTextColor;
            return this;
        }

        public Builder pastFutureEventColor(int pastFutureEventColor) {
            this.pastFutureEventColor = pastFutureEventColor;
            return this;
        }

        public Builder calendarCellBorderColor(int calendarCellBorderColor) {
            this.calendarCellBorderColor = calendarCellBorderColor;
            return this;
        }

        public Builder onDateSelectedListener(OnDateSelectedListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder calendarEvents(List<CalendarEvent> calendarEvents) {
            this.calendarEvents = calendarEvents;
            return this;
        }

        public CalendarAdapter create() {

            if (context == null) {
                throw new IllegalStateException("Context cannot be null");
            }

            Date startDate;
            if (this.startDate != null) {
                startDate = this.startDate;
            } else {
                startDate = new Date();
            }

            Date endDate;
            if (this.endDate != null) {
                endDate = this.endDate;
            } else {
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(startDate);
                dateCal.add(Calendar.MONTH, 3);
                endDate = dateCal.getTime();
            }

            int totalMonths = CalendarUtils.getNumberOfMonthsApartInclusive(startDate, endDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date[] months = new Date[totalMonths];
            for (int i = 0; i < totalMonths; i++) {
                months[i] = cal.getTime();
                cal.add(Calendar.MONTH, 1);
            }

            List<List<CalendarEvent>> calendarEventsInEachMonth = new ArrayList<List<CalendarEvent>>();
            if (calendarEvents != null) {
                Calendar monthCal = Calendar.getInstance();
                monthCal.setTime(months[0]);
                monthCal.add(Calendar.MONTH, -1);

                Calendar eventCal = Calendar.getInstance();
                for (int i = 0; i <= months.length + 1; i++) {
                    List<CalendarEvent> calendarEventsInMonth = new ArrayList<CalendarEvent>();
                    for (CalendarEvent calendarEvent : calendarEvents) {
                        eventCal.setTime(calendarEvent.startDate);
                        if (monthCal.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
                                monthCal.get(Calendar.MONTH) == eventCal.get(Calendar.MONTH)) {
                            calendarEventsInMonth.add(calendarEvent);
                        }
                    }
                    calendarEventsInEachMonth.add(calendarEventsInMonth);
                    monthCal.add(Calendar.MONTH, 1);
                }
            }

            daysOfWeekStrings = new String[7];
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            for (int i = 0; i < 7; i++) {
                daysOfWeekStrings[i] = dayOfWeekFormat.format(cal.getTime());
                cal.add(Calendar.DAY_OF_WEEK, 1);
            }

            if (titleTypeface == null) {
                titleTypeface = Typeface.DEFAULT;
            }

            if (daysOfWeekTypeface == null) {
                daysOfWeekTypeface = Typeface.DEFAULT;
            }

            if (calendarCellTypeface == null) {
                calendarCellTypeface = Typeface.DEFAULT;
            }

            int defaultTextColor = context.getResources().getColor(R.color.default_text_color);

            if (titleTextColor == -1) {
                titleTextColor = defaultTextColor;
            }

            if (daysOfWeekTextColor == -1) {
                daysOfWeekTextColor = defaultTextColor;
            }

            if (calendarCellTextColor == -1) {
                calendarCellTextColor = defaultTextColor;
            }

            if (eventColor == -1) {
                eventColor = context.getResources().getColor(R.color.default_event_color);
            }

            if (pastFutureCalendarCellBackgroundColor == -1) {
                pastFutureCalendarCellBackgroundColor = context.getResources()
                        .getColor(R.color.past_future_cell_background_color);
            }

            if (pastFutureCalendarCellTextColor == -1) {
                pastFutureCalendarCellTextColor = defaultTextColor;
            }

            if (pastFutureEventColor == -1) {
                pastFutureEventColor = defaultTextColor;
            }

            if (calendarCellBorderColor == -1) {
                calendarCellBorderColor = context.getResources().getColor(R.color.default_event_color);
            }

            return new CalendarAdapter(context, months, daysOfWeekStrings, titleTypeface,
                    daysOfWeekTypeface, calendarCellTypeface, titleTextColor,
                    daysOfWeekTextColor, calendarCellTextColor, eventColor,
                    pastFutureCalendarCellBackgroundColor, pastFutureCalendarCellTextColor,
                    pastFutureEventColor, calendarCellBorderColor, calendarEventsInEachMonth,
                    listener);
        }
    }
}
