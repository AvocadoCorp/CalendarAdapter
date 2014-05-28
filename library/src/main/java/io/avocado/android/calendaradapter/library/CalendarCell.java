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

    private Paint mEventPaint;
    private Paint mTextPaint;
    private Paint mPlusPaint;

    private ArrayList<Rect> mOddNumEventRects = new ArrayList<Rect>();
    private ArrayList<Rect> mEvenNumEventRects = new ArrayList<Rect>();

    private int[] mTextOrigin = new int[2];
    private Rect[] mPlusRects = new Rect[2];

    private String mDateText;

    private int mNumEvents;
    private int mNumSquaresToDraw; // Including plus sign

    private static final int MAX_EVENTS = 3;

    public CalendarCell(Context context) {
        this(context, null);
    }

    public CalendarCell(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize((int) context.getResources().getDimension(R.dimen.cell_text_size));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(context.getResources().getColor(R.color.default_text_color));

        mEventPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEventPaint.setStyle(Paint.Style.FILL);

        mPlusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setTextColor(int textColor) {
        mTextPaint.setColor(textColor);
    }

    public void setTypeface(Typeface typeface) {
        mTextPaint.setTypeface(typeface);
    }

    public void setDateText(String dateText) {
        mDateText = dateText;
    }

    public void setEventColor(int eventColor) {
        mEventPaint.setColor(eventColor);
        mPlusPaint.setColor(eventColor);
    }

    public void setNumEvents(int numEvents) {
        mNumEvents = numEvents;
        mNumSquaresToDraw = Math.min(numEvents, MAX_EVENTS);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTextOrigin[0] = (int) (w / 2.f);
        mTextOrigin[1] = 50;

        int size = (int) (.125f * w);
        int offsetFromBottom = (int) (.3f * h);
        int y = h - offsetFromBottom - size;
        int space = size + (int) (size / 2.f); // square width + one space

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
                mOddNumEventRects.add(new Rect(oddCenterX, y, oddCenterX + size, y + size));

            } else if (i % 2 == 0) {
                int spacesFromCenter = i / 2;

                int leftX = oddCenterX - spacesFromCenter * space;
                mOddNumEventRects.add(new Rect(leftX, y, leftX + size, y + size));

                int rightX = oddCenterX + spacesFromCenter * space;
                mOddNumEventRects.add(new Rect(rightX, y, rightX + size, y + size));
            }
        }

        int firstTwoSquaresWidth = space + size;
        int evenLeftCenterX = (int) (w / 2.f - firstTwoSquaresWidth / 2.f);
        int evenRightCenterX = evenLeftCenterX + space;
        for (int i = 0; i < maxNumEventsEven; i++) {

            if (i == 1) {
                mEvenNumEventRects.add(new Rect(evenLeftCenterX, y, evenLeftCenterX + size, y + size));
                mEvenNumEventRects.add(new Rect(evenRightCenterX, y, evenRightCenterX + size, y + size));
            } else if (i % 2 != 0) {
                int spacesFromCenter = i / 2;

                int leftX = evenLeftCenterX - spacesFromCenter * space;
                mEvenNumEventRects.add(new Rect(leftX, y, leftX + size, y + size));

                int rightX = evenRightCenterX + spacesFromCenter * space;
                mEvenNumEventRects.add(new Rect(rightX, y, rightX + size, y + size));
            }
        }

        // Plus sign

        Rect boundingPlusRect = MAX_EVENTS % 2 == 0 ? mEvenNumEventRects.get(MAX_EVENTS - 1)
                : mOddNumEventRects.get(MAX_EVENTS - 1);

        int strokeThickness = 4;

        int midX = boundingPlusRect.left + (int) (boundingPlusRect.width() / 2.f);
        int midY = boundingPlusRect.bottom - (int) (boundingPlusRect.height() / 2.f);

        // horizontal stroke
        mPlusRects[0] = new Rect(boundingPlusRect.left, midY - strokeThickness / 2,
                boundingPlusRect.right, midY + strokeThickness / 2);

        //vertical stroke
        mPlusRects[1] = new Rect(midX - strokeThickness / 2, boundingPlusRect.top,
                midX + strokeThickness / 2, boundingPlusRect.bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(mDateText, mTextOrigin[0], mTextOrigin[1], mTextPaint);

        ArrayList<Rect> eventRectsToDraw = mNumSquaresToDraw % 2 == 0 ? mEvenNumEventRects : mOddNumEventRects;

        for (int i = 0; i < mNumSquaresToDraw; i++) {

            if (i == mNumSquaresToDraw - 1 && mNumEvents > MAX_EVENTS) {

                canvas.drawRect(mPlusRects[0], mPlusPaint);
                canvas.drawRect(mPlusRects[1], mPlusPaint);

            } else {

                canvas.drawRect(eventRectsToDraw.get(i), mEventPaint);

            }
        }
    }
}
