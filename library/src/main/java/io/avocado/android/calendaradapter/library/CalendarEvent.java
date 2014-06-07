package io.avocado.android.calendaradapter.library;

import java.util.Date;

/**
 * Created by matthewlogan on 6/6/14.
 */
public class CalendarEvent {

    public Date startDate;
    public Date endDate;

    public CalendarEvent(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
