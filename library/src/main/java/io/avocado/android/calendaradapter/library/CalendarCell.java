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

    private ArrayList<Rect> mEventRects = new ArrayList<Rect>();
    private int[] mTextOrigin = new int[2];
    private Rect[] mPlusRects = new Rect[2];

    private String mDateText;

    private int mNumEvents;

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
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTextOrigin[0] = (int) (w / 2.f);
        mTextOrigin[1] = 50;

        int numEventsToShow = Math.min(mNumEvents, MAX_EVENTS);

        if (numEventsToShow > 0) {

            int size = (int) (.125f * w);

            int offsetFromBottom = (int) (.3f * h);

            int y = h - offsetFromBottom - size;

            int space = (int) (size / 2.f);

            int totalEventsWidth = numEventsToShow * size + (numEventsToShow - 1) * space;

            int startX = (int) (w / 2.f - totalEventsWidth / 2.f);

            for (int i = 0; i < numEventsToShow; i++) {

                int x = startX + i * (size + space);

                mEventRects.add(new Rect(x, y, x + size, y + size));
            }
        }

        if (numEventsToShow < mNumEvents) {

            Rect boundingRect = mEventRects.get(numEventsToShow - 1);

            int strokeThickness = 4;

            int midX = boundingRect.left + (int) (boundingRect.width() / 2.f);
            int midY = boundingRect.bottom - (int) (boundingRect.height() / 2.f);

            // horizontal stroke
            mPlusRects[0] = new Rect(boundingRect.left, midY - strokeThickness / 2,
                    boundingRect.right, midY + strokeThickness / 2);

            //vertical stroke
            mPlusRects[1] = new Rect(midX - strokeThickness / 2, boundingRect.top,
                    midX + strokeThickness / 2, boundingRect.bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(mDateText, mTextOrigin[0], mTextOrigin[1], mTextPaint);

        for (int i = 0; i < mEventRects.size(); i++) {

            if (i == mEventRects.size() - 1 && mNumEvents > MAX_EVENTS) {

                canvas.drawRect(mPlusRects[0], mPlusPaint);
                canvas.drawRect(mPlusRects[1], mPlusPaint);

            } else {
                canvas.drawRect(mEventRects.get(i), mEventPaint);
            }
        }
    }
}
