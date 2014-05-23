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
import java.util.Calendar;
import java.util.Date;
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

    private CalendarAdapter(Context context, Date[] months, String[] daysOfWeekStrings,
                            Typeface titleTypeface, Typeface daysOfWeekTypeface,
                            Typeface calendarCellTypeface, int titleTextColor,
                            int daysOfWeekTextColor, int calendarCellTextColor, int eventColor) {

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

            int dayOfWeekTextSize = (int) mContext.getResources().getDimension(
                    R.dimen.day_of_week_text_size);

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

            convertView.setTag(vh);
        }
        ViewHolder vh = (ViewHolder) convertView.getTag();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.getDefault());
        String prettyMonth = sdf.format(mMonths[position]);
        vh.titleView.setText(prettyMonth);

        for (int i = 0; i < vh.daysOfWeekView.getChildCount(); i++) {
            TextView dayLabel = (TextView) vh.daysOfWeekView.getChildAt(i);
            if (dayLabel != null) {
                dayLabel.setText(mDaysOfWeekStrings[i]);
            }
        }

        vh.calendarGrid.initCalendar(mMonths[position]);

        return convertView;
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

        private Builder daysOfWeekStrings(String[] daysOfWeekStrings) {
            mDaysOfWeekStrings = daysOfWeekStrings;
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

            if (mDaysOfWeekStrings == null) {
                mDaysOfWeekStrings = new String[]{
                        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
                };
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

            return new CalendarAdapter(mContext, months, mDaysOfWeekStrings, mTitleTypeface,
                    mDaysOfWeekTypeface, mCalendarCellTypeface, mTitleTextColor,
                    mDaysOfWeekTextColor, mCalendarCellTextColor, mEventColor);
        }
    }
}
