package io.avocado.android.calendaradapter.library;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by matthewlogan on 5/22/14.
 */
public class CalendarGrid extends LinearLayout implements View.OnClickListener {

    private Typeface typeface;
    private int textColor;

    private int eventColor;

    private Date someDateInMonth;

    private int pastFutureCalendarCellBackgroundColor;
    private int pastFutureCalendarCellTextColor;
    private int pastFutureEventColor;

    private int calendarCellBorderColor;

    private int todayCalendarCellBackgroundColor;

    public CalendarAdapter.OnDateSelectedListener listener;

    private int measureSpecToUse = 0;

    public CalendarGrid(Context context) {
        this(context, null);
    }

    public CalendarGrid(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);

        for (int i = 0; i < 6; i++) {
            LinearLayout calendarRow = new LinearLayout(context);
            calendarRow.setOrientation(HORIZONTAL);

            for (int j = 0; j < 7; j++) {
                CalendarCell calendarCell = new CalendarCell(context) {
                    @Override
                    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                        int currentSize = MeasureSpec.getSize(measureSpecToUse);
                        int newSize = MeasureSpec.getSize(widthMeasureSpec);
                        boolean useNewSize =
                                MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY &&
                                        newSize > 0 &&
                                        (currentSize == 0 || Math.abs(newSize - currentSize) > 1);

                        if (useNewSize) {
                            measureSpecToUse = widthMeasureSpec;
                        }

                        super.onMeasure(measureSpecToUse, measureSpecToUse);
                    }
                };

                LayoutParams cellLp = new LayoutParams(0, 0);
                cellLp.weight = 1;
                calendarCell.setLayoutParams(cellLp);

                if (i != 0 && i != 5 && j != 0 && j != 6) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.INSIDE);
                } else if (i == 0 && j == 0) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.TOP_LEFT_CORNER);
                } else if (i == 0 && j == 6) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.TOP_RIGHT_CORNER);
                } else if (i == 5 && j == 0) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.BOTTOM_LEFT_CORNER);
                } else if (i == 5 && j == 6) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.BOTTOM_RIGHT_CORNER);
                } else if (i == 0) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.TOP_EDGE);
                } else if (j == 0) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.LEFT_EDGE);
                } else if (i == 5) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.BOTTOM_EDGE);
                } else if (j == 6) {
                    calendarCell.setGridPosition(CalendarCell.GridPosition.RIGHT_EDGE);
                }

                calendarRow.addView(calendarCell);
            }

            addView(calendarRow);
        }
    }

    public void initCalendar(Date someDateInMonth, List<CalendarEvent> currentMonthCalendarEvents,
                             List<CalendarEvent> previousMonthCalendarEvents,
                             List<CalendarEvent> nextMonthCalendarEvents) {

        this.someDateInMonth = someDateInMonth;

        int daysInCurrentMonth = CalendarUtils.getNumberOfDaysInMonth(someDateInMonth);
        int daysInPreviousMonth = CalendarUtils.getNumberOfDaysInPreviousMonth(someDateInMonth);
        int daysToShowInPreviousMonthBeforeThisMonth
                = CalendarUtils.getNumberOfDaysToShowInPreviousMonthBeforeThisMonth(someDateInMonth);

        Calendar cal = Calendar.getInstance();

        int[] eventsPerDay = new int[42];

        boolean[] multiDayStarts = new boolean[42];
        boolean[] multiDayMids = new boolean[42];
        boolean[] multiDayEnds = new boolean[42];

        int startCalPos;
        int daysFromStartToEndDate;

        if (currentMonthCalendarEvents != null) {
            for (CalendarEvent calendarEvent : currentMonthCalendarEvents) {
                cal.setTime(calendarEvent.getStartDate());
                startCalPos = daysToShowInPreviousMonthBeforeThisMonth +
                        cal.get(Calendar.DAY_OF_MONTH) - 1;
                daysFromStartToEndDate = CalendarUtils.getNumberOfDaysInApartExclusive(
                        calendarEvent.getStartDate(), calendarEvent.getEndDate());

                if (calendarEvent.getEndDate() == null || daysFromStartToEndDate == 0) {
                    eventsPerDay[startCalPos] += 1;
                } else {
                    int endCalPos = Math.min(startCalPos + daysFromStartToEndDate, 41);
                    for (int i = startCalPos; i <= endCalPos; i++) {
                        if (i == startCalPos) {
                            multiDayStarts[i] = true;
                        } else if (i == endCalPos) {
                            multiDayEnds[i] = true;
                        } else {
                            multiDayMids[i] = true;
                        }
                    }
                }
            }
        }

        if (previousMonthCalendarEvents != null) {
            for (CalendarEvent calendarEvent : previousMonthCalendarEvents) {
                cal.setTime(calendarEvent.getStartDate());
                startCalPos = daysToShowInPreviousMonthBeforeThisMonth -
                        (daysInPreviousMonth - cal.get(Calendar.DAY_OF_MONTH)) - 1;
                daysFromStartToEndDate = CalendarUtils.getNumberOfDaysInApartExclusive(
                        calendarEvent.getStartDate(), calendarEvent.getEndDate());

                if (calendarEvent.getEndDate() == null || daysFromStartToEndDate == 0) {
                    if (startCalPos >= 0) {
                        eventsPerDay[startCalPos] += 1;
                    }
                } else {
                    int endCalPos = Math.min(startCalPos + daysFromStartToEndDate, 41);
                    for (int i = startCalPos; i <= endCalPos; i++) {
                        if (i >= 0) {
                            if (i == startCalPos) {
                                multiDayStarts[i] = true;
                            } else if (i == endCalPos) {
                                multiDayEnds[i] = true;
                            } else {
                                multiDayMids[i] = true;
                            }
                        }
                    }
                }
            }
        }

        if (nextMonthCalendarEvents != null) {
            for (CalendarEvent calendarEvent : nextMonthCalendarEvents) {
                cal.setTime(calendarEvent.getStartDate());
                startCalPos = daysToShowInPreviousMonthBeforeThisMonth + daysInCurrentMonth
                        + cal.get(Calendar.DAY_OF_MONTH) - 1;
                daysFromStartToEndDate = CalendarUtils.getNumberOfDaysInApartExclusive(
                        calendarEvent.getStartDate(), calendarEvent.getEndDate());

                if (calendarEvent.getEndDate() == null || daysFromStartToEndDate == 0) {
                    if (startCalPos < 42) {
                        eventsPerDay[startCalPos] += 1;
                    }
                } else {
                    int endCalPos = Math.min(startCalPos + daysFromStartToEndDate, 41);
                    for (int i = startCalPos; i <= endCalPos; i++) {
                        if (i == startCalPos) {
                            multiDayStarts[i] = true;
                        } else if (i == endCalPos) {
                            multiDayEnds[i] = true;
                        } else {
                            multiDayMids[i] = true;
                        }
                    }
                }
            }
        }

        Calendar todayCal = Calendar.getInstance();
        int todayNum = todayCal.get(Calendar.DAY_OF_MONTH);
        boolean todayInCurrentMonth = CalendarUtils.isSameMonth(someDateInMonth, new Date());

        for (int calPosition = 0; calPosition < 42; calPosition++) {

            int rowNum = calPosition / 7;
            int colNum = calPosition - 7 * rowNum;

            LinearLayout row = (LinearLayout) getChildAt(rowNum);

            CalendarCell calendarCell = (CalendarCell) row.getChildAt(colNum);
            if (calendarCell == null) {
                continue;
            }

            if (typeface != null) {
                calendarCell.setTypeface(typeface);
            }

            int dayNum;
            CalendarCell.RelativeMonth relativeMonth;
            boolean today = false;

            if (calPosition < daysToShowInPreviousMonthBeforeThisMonth) {

                dayNum = daysInPreviousMonth - daysToShowInPreviousMonthBeforeThisMonth + calPosition + 1;
                relativeMonth = CalendarCell.RelativeMonth.PREVIOUS;

            } else if (calPosition >= daysToShowInPreviousMonthBeforeThisMonth &&
                    calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth <= daysInCurrentMonth) {

                dayNum = calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth;
                relativeMonth = CalendarCell.RelativeMonth.CURRENT;

                if (todayInCurrentMonth && dayNum == todayNum) {
                    today = true;
                }

            } else {

                dayNum = calPosition + 1 - daysToShowInPreviousMonthBeforeThisMonth - daysInCurrentMonth;
                relativeMonth = CalendarCell.RelativeMonth.NEXT;
            }

            calendarCell.setDayOfMonth(dayNum);
            calendarCell.setTextColor(textColor);
            calendarCell.setEventColor(eventColor);
            calendarCell.setPastFutureCalendarCellBackgroundColor(pastFutureCalendarCellBackgroundColor);
            calendarCell.setPastFutureCalendarCellTextColor(pastFutureCalendarCellTextColor);
            calendarCell.setPastFutureEventColor(pastFutureEventColor);
            calendarCell.setBorderColor(calendarCellBorderColor);
            calendarCell.setTodayBackgroundColor(todayCalendarCellBackgroundColor);
            calendarCell.setRelativeMonth(relativeMonth);
            calendarCell.setToday(today);
            calendarCell.setOnClickListener(this);

            calendarCell.setNumEvents(eventsPerDay[calPosition]);

            CalendarCell.MultiDayPosition mdp;
            if (multiDayStarts[calPosition] && !multiDayMids[calPosition] && !multiDayEnds[calPosition]) {
                mdp = CalendarCell.MultiDayPosition.START;
            } else if (!multiDayStarts[calPosition] && !multiDayMids[calPosition] && multiDayEnds[calPosition]) {
                mdp = CalendarCell.MultiDayPosition.END;
            } else if (!multiDayStarts[calPosition] && !multiDayMids[calPosition] && !multiDayEnds[calPosition]) {
                mdp = CalendarCell.MultiDayPosition.NONE;
            } else {
                mdp = CalendarCell.MultiDayPosition.MID;
            }

            calendarCell.setMultiDayPosition(mdp);
        }
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setEventColor(int eventColor) {
        this.eventColor = eventColor;
    }

    public void setOnDateSelectedListener(CalendarAdapter.OnDateSelectedListener listener) {
        this.listener = listener;
    }

    public void setPastFutureCalendarCellBackgroundColor(int pastFutureCalendarCellBackgroundColor) {
        this.pastFutureCalendarCellBackgroundColor = pastFutureCalendarCellBackgroundColor;
    }

    public void setPastFutureCalendarCellTextColor(int pastFutureCalendarCellTextColor) {
        this.pastFutureCalendarCellTextColor = pastFutureCalendarCellTextColor;
    }

    public void setPastFutureEventColor(int pastFutureEventColor) {
        this.pastFutureEventColor = pastFutureEventColor;
    }

    public void setCalendarCellBorderColor(int calendarCellBorderColor) {
        this.calendarCellBorderColor = calendarCellBorderColor;
    }

    public void setTodayCalendarCellBackgroundColor(int todayCalendarCellBackgroundColor) {
        this.todayCalendarCellBackgroundColor = todayCalendarCellBackgroundColor;
    }

    @Override
    public void onClick(View v) {
        CalendarCell cell = (CalendarCell) v;

        Calendar cal = Calendar.getInstance();
        cal.setTime(someDateInMonth);
        cal.set(Calendar.DAY_OF_MONTH, cell.getDayOfMonth());

        if (cell.getRelativeMonth() == CalendarCell.RelativeMonth.PREVIOUS) {
            cal.add(Calendar.MONTH, -1);
        } else if (cell.getRelativeMonth() == CalendarCell.RelativeMonth.NEXT) {
            cal.add(Calendar.MONTH, 1);
        }

        listener.onDateSelected(cal.getTime());
    }
}
