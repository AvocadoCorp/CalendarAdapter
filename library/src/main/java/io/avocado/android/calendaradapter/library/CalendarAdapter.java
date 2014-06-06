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

    private List<List<Date>> eventDatesInEachMonth;

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
                            int calendarCellBorderColor, List<List<Date>> eventDatesInEachMonth,
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
        this.eventDatesInEachMonth = eventDatesInEachMonth;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return months.length;
    }

    @Override
    public Object getItem(int position) {
        return eventDatesInEachMonth != null ? eventDatesInEachMonth.get(position) : null;
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

        List<Date> currentMonthEventDates = null;
        if (eventDatesInEachMonth.size() > position + 1) {
            currentMonthEventDates = eventDatesInEachMonth.get(position + 1);
        }

        List<Date> previousMonthEventDates = null;
        if (eventDatesInEachMonth.size() > position) {
            previousMonthEventDates = eventDatesInEachMonth.get(position);
        }

        List<Date> nextMonthEventDates = null;
        if (eventDatesInEachMonth.size() > position + 2) {
            nextMonthEventDates = eventDatesInEachMonth.get(position + 2);
        }

        vh.calendarGrid.initCalendar(months[position], currentMonthEventDates,
                previousMonthEventDates, nextMonthEventDates);

        return convertView;
    }

    public void addEventDate(Date eventDate) {
        if (eventDatesInEachMonth == null) {
            eventDatesInEachMonth = new ArrayList<List<Date>>();
        }

        Calendar monthCal = Calendar.getInstance();
        monthCal.setTime(months[0]);
        monthCal.add(Calendar.MONTH, -1);

        Calendar eventCal = Calendar.getInstance();
        eventCal.setTime(eventDate);

        for (int i = 0; i <= months.length + 1; i++) {
            if (eventDatesInEachMonth.get(i) == null) {
                eventDatesInEachMonth.add(new ArrayList<Date>());
            }

            if (monthCal.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
                    monthCal.get(Calendar.MONTH) == eventCal.get(Calendar.MONTH)) {
                eventDatesInEachMonth.get(i).add(eventDate);
            }

            monthCal.add(Calendar.MONTH, 1);
        }
    }

    public void setEventDates(List<Date> eventDates) {
        if (eventDatesInEachMonth == null) {
            eventDatesInEachMonth = new ArrayList<List<Date>>();
        } else {
            eventDatesInEachMonth.clear();
        }

        Calendar monthCal = Calendar.getInstance();
        monthCal.setTime(months[0]);
        monthCal.add(Calendar.MONTH, -1);

        Calendar eventCal = Calendar.getInstance();
        for (int i = 0; i <= months.length + 1; i++) {
            List<Date> eventDatesInMonth = new ArrayList<Date>();
            for (Date eventDate : eventDates) {
                eventCal.setTime(eventDate);
                if (monthCal.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
                        monthCal.get(Calendar.MONTH) == eventCal.get(Calendar.MONTH)) {
                    eventDatesInMonth.add(eventDate);
                }
            }
            eventDatesInEachMonth.add(eventDatesInMonth);
            monthCal.add(Calendar.MONTH, 1);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public static class Builder {

        private Context mContext;

        private Date mStartDate;
        private Date mEndDate;

        private String[] mDaysOfWeekStrings;

        private Typeface mTitleTypeface;
        private Typeface mDaysOfWeekTypeface;
        private Typeface mCalendarCellTypeface;

        private int mTitleTextColor = -1;
        private int mDaysOfWeekTextColor = -1;
        private int mCalendarCellTextColor = -1;

        private int mEventColor = -1;

        private int mPastFutureCalendarCellBackgroundColor = -1;
        private int mPastFutureCalendarCellTextColor = -1;
        private int mPastFutureEventColor = -1;

        private int mCalendarCellBorderColor = -1;

        private List<Date> mEventDates;

        private OnDateSelectedListener mListener;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder startDate(Date someDateInStartMonth) {
            mStartDate = someDateInStartMonth;
            return this;
        }

        public Builder endDate(Date someDateInEndMonth) {
            mEndDate = someDateInEndMonth;
            return this;
        }

        public Builder titleTypeface(Typeface titleTypeface) {
            mTitleTypeface = titleTypeface;
            return this;
        }

        public Builder daysOfWeekTypeface(Typeface daysOfWeekTypeface) {
            mDaysOfWeekTypeface = daysOfWeekTypeface;
            return this;
        }

        public Builder calendarCellTypeface(Typeface calendarCellTypeface) {
            mCalendarCellTypeface = calendarCellTypeface;
            return this;
        }

        public Builder titleTextColor(int titleTextColor) {
            mTitleTextColor = titleTextColor;
            return this;
        }

        public Builder daysOfWeekTextColor(int daysOfWeekTextColor) {
            mDaysOfWeekTextColor = daysOfWeekTextColor;
            return this;
        }

        public Builder calendarCellTextColor(int calendarCellTextColor) {
            mCalendarCellTextColor = calendarCellTextColor;
            return this;
        }

        public Builder eventColor(int eventColor) {
            mEventColor = eventColor;
            return this;
        }

        public Builder pastFutureCalendarCellBackgroundColor(int pastFutureCalendarCellBackgroundColor) {
            mPastFutureCalendarCellBackgroundColor = pastFutureCalendarCellBackgroundColor;
            return this;
        }

        public Builder pastFutureCalendarCellTextColor(int pastFutureCalendarCellTextColor) {
            mPastFutureCalendarCellTextColor = pastFutureCalendarCellTextColor;
            return this;
        }

        public Builder pastFutureEventColor(int pastFutureEventColor) {
            mPastFutureEventColor = pastFutureEventColor;
            return this;
        }

        public Builder calendarCellBorderColor(int calendarCellBorderColor) {
            mCalendarCellBorderColor = calendarCellBorderColor;
            return this;
        }

        public Builder onDateSelectedListener(OnDateSelectedListener listener) {
            mListener = listener;
            return this;
        }

        public Builder eventDates(List<Date> eventDates) {
            mEventDates = eventDates;
            return this;
        }

        public CalendarAdapter create() {

            if (mContext == null) {
                throw new IllegalStateException("Context cannot be null");
            }

            Date startDate;
            if (mStartDate != null) {
                startDate = mStartDate;
            } else {
                startDate = new Date();
            }

            Date endDate;
            if (mEndDate != null) {
                endDate = mEndDate;
            } else {
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(startDate);
                dateCal.add(Calendar.MONTH, 3);
                endDate = dateCal.getTime();
            }

            int totalMonths = CalendarUtils.getNumberOfMonthsApartInclusive(startDate, endDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(mStartDate);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date[] months = new Date[totalMonths];
            for (int i = 0; i < totalMonths; i++) {
                months[i] = cal.getTime();
                cal.add(Calendar.MONTH, 1);
            }

            List<List<Date>> eventDatesInEachMonth = new ArrayList<List<Date>>();
            if (mEventDates != null) {
                Calendar monthCal = Calendar.getInstance();
                monthCal.setTime(months[0]);
                monthCal.add(Calendar.MONTH, -1);

                Calendar eventCal = Calendar.getInstance();
                for (int i = 0; i <= months.length + 1; i++) {
                    List<Date> eventDatesInMonth = new ArrayList<Date>();
                    for (Date eventDate : mEventDates) {
                        eventCal.setTime(eventDate);
                        if (monthCal.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
                                monthCal.get(Calendar.MONTH) == eventCal.get(Calendar.MONTH)) {
                            eventDatesInMonth.add(eventDate);
                        }
                    }
                    eventDatesInEachMonth.add(eventDatesInMonth);
                    monthCal.add(Calendar.MONTH, 1);
                }
            }

            mDaysOfWeekStrings = new String[7];
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            for (int i = 0; i < 7; i++) {
                mDaysOfWeekStrings[i] = dayOfWeekFormat.format(cal.getTime());
                cal.add(Calendar.DAY_OF_WEEK, 1);
            }

            if (mTitleTypeface == null) {
                mTitleTypeface = Typeface.DEFAULT;
            }

            if (mDaysOfWeekTypeface == null) {
                mDaysOfWeekTypeface = Typeface.DEFAULT;
            }

            if (mCalendarCellTypeface == null) {
                mCalendarCellTypeface = Typeface.DEFAULT;
            }

            int defaultTextColor = mContext.getResources().getColor(R.color.default_text_color);

            if (mTitleTextColor == -1) {
                mTitleTextColor = defaultTextColor;
            }

            if (mDaysOfWeekTextColor == -1) {
                mDaysOfWeekTextColor = defaultTextColor;
            }

            if (mCalendarCellTextColor == -1) {
                mCalendarCellTextColor = defaultTextColor;
            }

            if (mEventColor == -1) {
                mEventColor = mContext.getResources().getColor(R.color.default_event_color);
            }

            if (mPastFutureCalendarCellBackgroundColor == -1) {
                mPastFutureCalendarCellBackgroundColor = mContext.getResources()
                        .getColor(R.color.gray);
            }

            if (mPastFutureCalendarCellTextColor == -1) {
                mPastFutureCalendarCellTextColor = defaultTextColor;
            }

            if (mPastFutureEventColor == -1) {
                mPastFutureEventColor = defaultTextColor;
            }

            if (mCalendarCellBorderColor == -1) {
                mCalendarCellBorderColor = mContext.getResources()
                        .getColor(R.color.default_cell_border_color);
            }

            return new CalendarAdapter(mContext, months, mDaysOfWeekStrings, mTitleTypeface,
                    mDaysOfWeekTypeface, mCalendarCellTypeface, mTitleTextColor,
                    mDaysOfWeekTextColor, mCalendarCellTextColor, mEventColor,
                    mPastFutureCalendarCellBackgroundColor, mPastFutureCalendarCellTextColor,
                    mPastFutureEventColor, mCalendarCellBorderColor, eventDatesInEachMonth,
                    mListener);
        }
    }
}
