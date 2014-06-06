package io.avocado.android.calendaradapter.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by matthewlogan on 5/22/14.
 */
public class CalendarCell extends View {

    private Paint eventPaint;
    private Paint textPaint;
    private Paint plusPaint;
    private Paint pastFutureBackgroundPaint;
    private Paint borderPaint;

    private ArrayList<Rect> oddNumEventRects = new ArrayList<Rect>();
    private ArrayList<Rect> evenNumEventRects = new ArrayList<Rect>();

    private ArrayList<Rect> eventRectsToDraw;

    private int[] textOrigin = new int[2];
    private Rect[] plusRects = new Rect[2];
    private Rect borderRect = new Rect();

    private int dayOfMonth;
    private String dateText;

    private int numEvents;
    private int numRectsToDraw; // Including plus sign

    private static final int MAX_EVENTS = 3;
    private final int PLUS_STROKE_THICKNESS;

    public enum RelativeMonth {
        PREVIOUS, CURRENT, NEXT
    }

    public enum GridPosition {
        LEFT_EDGE, TOP_LEFT_CORNER, TOP_EDGE, TOP_RIGHT_CORNER, RIGHT_EDGE, BOTTOM_RIGHT_CORNER,
        BOTTOM_EDGE, BOTTOM_LEFT_CORNER, INSIDE
    }

    private boolean shouldDrawLeftBorder;
    private boolean shouldDrawTopBorder;

    private RelativeMonth relativeMonth;

    private int textColor;
    private int eventColor;

    private int pastFutureCalendarCellTextColor;
    private int pastFutureEventColor;

    public CalendarCell(Context context) {
        this(context, null);
    }

    public CalendarCell(Context context, AttributeSet attrs) {
        super(context, attrs);

        pastFutureBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize((int) context.getResources().getDimension(R.dimen.cell_text_size));
        textPaint.setTextAlign(Paint.Align.CENTER);

        eventPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        eventPaint.setStyle(Paint.Style.FILL);

        plusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        PLUS_STROKE_THICKNESS = (int) getResources().getDimension(R.dimen.plus_stroke_thickness);

        int height = (int) (getResources().getDisplayMetrics().widthPixels / 7.f);
        setMinimumHeight(height);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTypeface(Typeface typeface) {
        this.textPaint.setTypeface(typeface);
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        dateText = String.valueOf(dayOfMonth);
    }

    public void setEventColor(int eventColor) {
        this.eventColor = eventColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderPaint.setColor(borderColor);
    }

    public void setNumEvents(int numEvents) {
        this.numEvents = numEvents;
        numRectsToDraw = Math.min(numEvents, MAX_EVENTS);
        eventRectsToDraw = numRectsToDraw % 2 == 0 ? evenNumEventRects : oddNumEventRects;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        borderRect = new Rect(0, h, w, 0);

        textOrigin[0] = (int) (w / 2.f);
        textOrigin[1] = (int) (h / 2.f);

        int size = (int) (.125f * w);
        int offsetFromBottom = (int) (.1875f * h);
        int y = h - offsetFromBottom - size;
        int space = (int) (1.5f * size); // square width + one space

        int maxNumEventsOdd;
        int maxNumEventsEven;
        if (MAX_EVENTS % 2 == 0) {
            maxNumEventsEven = MAX_EVENTS;
            maxNumEventsOdd = MAX_EVENTS - 1;
        } else {
            maxNumEventsOdd = MAX_EVENTS;
            maxNumEventsEven = MAX_EVENTS - 1;
        }

        int oddCenterX = (int) (w / 2.f - size / 2.f);
        for (int i = 0; i < maxNumEventsOdd; i++) {

            if (i == 0) {
                oddNumEventRects.add(new Rect(oddCenterX, y, oddCenterX + size, y + size));

            } else if (i % 2 == 0) {
                int spacesFromCenter = i / 2;

                int leftX = oddCenterX - spacesFromCenter * space;
                oddNumEventRects.add(new Rect(leftX, y, leftX + size, y + size));

                int rightX = oddCenterX + spacesFromCenter * space;
                oddNumEventRects.add(new Rect(rightX, y, rightX + size, y + size));
            }
        }

        int firstTwoSquaresWidth = space + size;
        int evenLeftCenterX = (int) (w / 2.f - firstTwoSquaresWidth / 2.f);
        int evenRightCenterX = evenLeftCenterX + space;

        for (int i = 0; i < maxNumEventsEven; i++) {

            if (i == 1) {
                evenNumEventRects.add(new Rect(evenLeftCenterX, y, evenLeftCenterX + size, y + size));
                evenNumEventRects.add(new Rect(evenRightCenterX, y, evenRightCenterX + size, y + size));

            } else if (i % 2 != 0) {
                int spacesFromCenter = i / 2;

                int leftX = evenLeftCenterX - spacesFromCenter * space;
                evenNumEventRects.add(new Rect(leftX, y, leftX + size, y + size));

                int rightX = evenRightCenterX + spacesFromCenter * space;
                evenNumEventRects.add(new Rect(rightX, y, rightX + size, y + size));
            }
        }

        // Plus sign

        Rect boundingPlusRect = MAX_EVENTS % 2 == 0 ? evenNumEventRects.get(MAX_EVENTS - 1)
                : oddNumEventRects.get(MAX_EVENTS - 1);

        int midX = boundingPlusRect.left + (int) (boundingPlusRect.width() / 2.f);
        int midY = boundingPlusRect.bottom - (int) (boundingPlusRect.height() / 2.f);

        // horizontal stroke
        plusRects[0] = new Rect(boundingPlusRect.left, midY - PLUS_STROKE_THICKNESS / 2,
                boundingPlusRect.right, midY + PLUS_STROKE_THICKNESS / 2);

        //vertical stroke
        plusRects[1] = new Rect(midX - PLUS_STROKE_THICKNESS / 2, boundingPlusRect.top,
                midX + PLUS_STROKE_THICKNESS / 2, boundingPlusRect.bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (relativeMonth != RelativeMonth.CURRENT) {
            canvas.drawPaint(pastFutureBackgroundPaint);
        }

        if (shouldDrawLeftBorder) {
            // Left stroke
            canvas.drawLine(borderRect.left, borderRect.bottom, borderRect.left, borderRect.top,
                    borderPaint);
        }

        if (shouldDrawTopBorder) {
            // Top stroke
            canvas.drawLine(borderRect.left, 0, borderRect.right, 0, borderPaint);
        }

        // Bottom stroke
        canvas.drawLine(borderRect.left, borderRect.top, borderRect.right,
                borderRect.top, borderPaint);

        // Right stroke
        canvas.drawLine(borderRect.right, borderRect.bottom, borderRect.right,
                borderRect.top, borderPaint);

        canvas.drawText(dateText, textOrigin[0], textOrigin[1], textPaint);

        for (int i = 0; i < numRectsToDraw; i++) {
            if (i == numRectsToDraw - 1 && numEvents > MAX_EVENTS) {
                canvas.drawRect(plusRects[0], plusPaint);
                canvas.drawRect(plusRects[1], plusPaint);
            } else {
                canvas.drawRect(eventRectsToDraw.get(i), eventPaint);
            }
        }
    }

    public void setPastFutureCalendarCellBackgroundColor(int pastFutureCalendarCellBackgroundColor) {
        pastFutureBackgroundPaint.setColor(pastFutureCalendarCellBackgroundColor);
    }

    public void setPastFutureCalendarCellTextColor(int pastFutureCalendarCellTextColor) {
        this.pastFutureCalendarCellTextColor = pastFutureCalendarCellTextColor;
    }

    public void setPastFutureEventColor(int pastFutureEventColor) {
        this.pastFutureEventColor = pastFutureEventColor;
    }

    public void setRelativeMonth(RelativeMonth relativeMonth) {
        this.relativeMonth = relativeMonth;

        if (relativeMonth == RelativeMonth.CURRENT) {
            textPaint.setColor(textColor);
            eventPaint.setColor(eventColor);
            plusPaint.setColor(eventColor);
        } else {
            textPaint.setColor(pastFutureCalendarCellTextColor);
            eventPaint.setColor(pastFutureEventColor);
            plusPaint.setColor(pastFutureEventColor);
        }
    }

    public void setGridPosition(GridPosition gridPosition) {
        shouldDrawLeftBorder = gridPosition == GridPosition.TOP_LEFT_CORNER ||
                gridPosition == GridPosition.LEFT_EDGE ||
                gridPosition == GridPosition.BOTTOM_LEFT_CORNER;

        shouldDrawTopBorder = gridPosition == GridPosition.TOP_LEFT_CORNER ||
                gridPosition == GridPosition.TOP_EDGE ||
                gridPosition == GridPosition.TOP_RIGHT_CORNER;
    }

    public RelativeMonth getRelativeMonth() {
        return relativeMonth;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }
}
