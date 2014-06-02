package io.avocado.android.calendaradapter.library;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
public class CalendarAdapter extends BaseAdapter {

    private Context mContext;

    private Date[] mMonths;

    private String[] mDaysOfWeekStrings;

    private Typeface mTitleTypeface;
    private Typeface mDaysOfWeekTypeface;
    private Typeface mCalendarCellTypeface;

    private int mTitleTextColor;
    private int mDaysOfWeekTextColor;
    private int mCalendarCellTextColor;

    private int mEventColor;

    private int mPastFutureCalendarCellBackgroundColor;
    private int mPastFutureCalendarCellTextColor;
    private int mPastFutureEventColor;

    private List<List<Date>> mEventDatesInEachMonth;

    private OnDateSelectedListener mListener;

    private static SimpleDateFormat mMonthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
    private static SimpleDateFormat mDayOfWeekFormat = new SimpleDateFormat("EEE", Locale.getDefault());

    public interface OnDateSelectedListener {
        public void onDateSelected(Date date);
    }

    private CalendarAdapter(Context context, Date[] months, String[] daysOfWeekStrings,
                            Typeface titleTypeface, Typeface daysOfWeekTypeface,
                            Typeface calendarCellTypeface, int titleTextColor,
                            int daysOfWeekTextColor, int calendarCellTextColor, int eventColor,
                            int pastFutureCalendarCellBackgroundColor,
                            int pastFutureCalendarCellTextColor, int pastFutureEventColor,
                            List<List<Date>> eventDatesInEachMonth,
                            OnDateSelectedListener listener) {

        mContext = context;
        mMonths = months;
        mDaysOfWeekStrings = daysOfWeekStrings;
        mTitleTypeface = titleTypeface;
        mDaysOfWeekTypeface = daysOfWeekTypeface;
        mCalendarCellTypeface = calendarCellTypeface;
        mTitleTextColor = titleTextColor;
        mDaysOfWeekTextColor = daysOfWeekTextColor;
        mCalendarCellTextColor = calendarCellTextColor;
        mEventColor = eventColor;
        mPastFutureCalendarCellBackgroundColor = pastFutureCalendarCellBackgroundColor;
        mPastFutureCalendarCellTextColor = pastFutureCalendarCellTextColor;
        mPastFutureEventColor = pastFutureEventColor;
        mEventDatesInEachMonth = eventDatesInEachMonth;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mMonths.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.calendar_page, parent, false);
            if (convertView == null) {
                return null;
            }

            ViewHolder vh = new ViewHolder();

            vh.titleView = (TextView) convertView.findViewById(R.id.month_title);
            vh.titleView.setTypeface(mTitleTypeface);
            vh.titleView.setTextColor(mTitleTextColor);
            vh.titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    (int) mContext.getResources().getDimension(R.dimen.title_text_size));

            vh.daysOfWeekView = (LinearLayout) convertView.findViewById(R.id.days_of_week);

            for (int i = 0; i < vh.daysOfWeekView.getChildCount(); i++) {
                TextView dayLabel = (TextView) vh.daysOfWeekView.getChildAt(i);
                if (dayLabel != null) {
                    dayLabel.setText(mDaysOfWeekStrings[i]);
                }
            }

            int dayOfWeekTextSize = (int) mContext.getResources().getDimension(R.dimen.day_of_week_text_size);

            for (int i = 0; i < vh.daysOfWeekView.getChildCount(); i++) {
                TextView dayLabel = (TextView) vh.daysOfWeekView.getChildAt(i);
                if (dayLabel != null) {
                    if (mDaysOfWeekTypeface != null) {
                        dayLabel.setTypeface(mDaysOfWeekTypeface);
                    }

                    if (mDaysOfWeekTextColor != -1) {
                        dayLabel.setTextColor(mDaysOfWeekTextColor);
                    }

                    dayLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, dayOfWeekTextSize);
                }
            }

            vh.calendarGrid = (CalendarGrid) convertView.findViewById(R.id.month_grid);
            vh.calendarGrid.setTypeface(mCalendarCellTypeface);
            vh.calendarGrid.setTextColor(mCalendarCellTextColor);
            vh.calendarGrid.setEventColor(mEventColor);
            vh.calendarGrid.setPastFutureCalendarCellBackgroundColor(mPastFutureCalendarCellBackgroundColor);
            vh.calendarGrid.setPastFutureCalendarCellTextColor(mPastFutureCalendarCellTextColor);
            vh.calendarGrid.setPastFutureEventColor(mPastFutureEventColor);
            vh.calendarGrid.setOnDateSelectedListener(mListener);

            convertView.setTag(vh);
        }
        ViewHolder vh = (ViewHolder) convertView.getTag();

        String prettyMonth = mMonthFormat.format(mMonths[position]);
        vh.titleView.setText(prettyMonth);

        vh.calendarGrid.initCalendar(mMonths[position], mEventDatesInEachMonth.get(position + 1),
                mEventDatesInEachMonth.get(position), mEventDatesInEachMonth.get(position + 2));

        return convertView;
    }

    public void addEventDate(Date eventDate) {
        Calendar eventCal = Calendar.getInstance();
        eventCal.setTime(eventDate);

        Calendar monthCal = Calendar.getInstance();
        monthCal.setTime(mMonths[0]);
        monthCal.add(Calendar.MONTH, -1);

        for (List<Date> eventDatesInMonth : mEventDatesInEachMonth) {
            if (monthCal.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
                    monthCal.get(Calendar.MONTH) == eventCal.get(Calendar.MONTH)) {

                eventDatesInMonth.add(eventDate);
            }

            monthCal.add(Calendar.MONTH, 1);
        }
    }

    public void addEventDates(List<Date> eventDates) {
        for (Date eventDate : eventDates) {
            addEventDate(eventDate);
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

            mDaysOfWeekStrings = new String[7];
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            for (int i = 0; i < 7; i++) {
                mDaysOfWeekStrings[i] = mDayOfWeekFormat.format(cal.getTime());
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
                mPastFutureCalendarCellBackgroundColor = mContext.getResources().getColor(R.color.gray);
            }

            if (mPastFutureCalendarCellTextColor == -1) {
                mPastFutureCalendarCellTextColor = defaultTextColor;
            }

            if (mPastFutureEventColor == -1) {
                mPastFutureEventColor = defaultTextColor;
            }

            return new CalendarAdapter(mContext, months, mDaysOfWeekStrings, mTitleTypeface,
                    mDaysOfWeekTypeface, mCalendarCellTypeface, mTitleTextColor,
                    mDaysOfWeekTextColor, mCalendarCellTextColor, mEventColor,
                    mPastFutureCalendarCellBackgroundColor, mPastFutureCalendarCellTextColor,
                    mPastFutureEventColor, eventDatesInEachMonth, mListener);
        }
    }
}
