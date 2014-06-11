package io.avocado.android.calendaradapter.app;

import java.util.Date;

import io.avocado.android.calendaradapter.library.CalendarEvent;

/**
 * Created by matthewlogan on 6/11/14.
 */
public class TestEvent implements CalendarEvent {

    private Date startDate;
    private Date endDate;

    public TestEvent(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }
}
