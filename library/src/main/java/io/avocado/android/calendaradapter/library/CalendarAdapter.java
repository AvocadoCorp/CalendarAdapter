package io.avocado.android.calendaradapter.library;

import android.content.Context;
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

    private String[] mDaysOfWeek =  {
        "Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"
    };

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
            convertView = li.inflate(R.layout.calendar_page, null);
            if (convertView == null) {
                return null;
            }

            ViewHolder vh = new ViewHolder();
            vh.titleView = (TextView) convertView.findViewById(R.id.month_title);
            vh.daysOfWeekView = (LinearLayout) convertView.findViewById(R.id.days_of_week);
            vh.calendarGrid = (CalendarGrid) convertView.findViewById(R.id.month_grid);
            convertView.setTag(vh);
        }

        ViewHolder vh = (ViewHolder) convertView.getTag();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.getDefault());
        String prettyMonth = sdf.format(mMonths[position]);

        vh.titleView.setText(prettyMonth);

        for (int i = 0; i < vh.daysOfWeekView.getChildCount(); i++) {
            TextView dayLabel = (TextView) vh.daysOfWeekView.getChildAt(i);
            if (dayLabel != null) {
                dayLabel.setText(mDaysOfWeek[i]);
            }
        }

        vh.calendarGrid.initCalendar(mMonths[position]);

        return convertView;
    }

    public static class Builder {

        private Context mContext;

        private Date mStartDate;
        private Date mEndDate;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder startDate(Date startDate) {
            mStartDate = startDate;
            return this;
        }

        public Builder endDate(Date endDate) {
            mEndDate = endDate;
            return this;
        }

        public CalendarAdapter create() {
            CalendarAdapter calendarAdapter = new CalendarAdapter();
            calendarAdapter.mContext = mContext;

            int totalMonths = CalendarUtils.getNumberOfMonthsApartInclusive(mStartDate, mEndDate);
            Date[] months = new Date[totalMonths];

            Calendar cal = Calendar.getInstance();
            cal.setTime(mStartDate);
            cal.set(Calendar.DAY_OF_MONTH, 1);

            for (int i = 0; i < totalMonths; i++) {
                months[i] = cal.getTime();
                cal.add(Calendar.MONTH, 1);
            }

            calendarAdapter.mMonths = months;

            return calendarAdapter;
        }
    }
}
