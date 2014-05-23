package io.avocado.android.calendaradapter.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

/**
 * Created by matthewlogan on 5/22/14.
 */
public class CalendarCell extends LinearLayout {

    private Paint mEventPaint;

    private Paint mTextPaint;

    private String mDateText;

    private int mNumEvents;

    public CalendarCell(Context context) {
        this(context, null);
    }

    public CalendarCell(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize((int) context.getResources().getDimension(R.dimen.cell_text_size));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(context.getResources().getColor(R.color.default_text_color));

        mEventPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEventPaint.setStyle(Paint.Style.FILL);
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
    }

    public void setNumEvents(int numEvents) {
        mNumEvents = numEvents;
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        canvas.drawText(mDateText, width / 2.f, 50, mTextPaint);

        if (mNumEvents > 0) {

            int size = (int) (.15f * width);

            int x = (int) (width / 2.f - size / 2.f);

            int offsetFromBottom = (int) (.3f * height);

            int y = height - offsetFromBottom - size;

            canvas.drawRect(x, y, x + size, y + size, mEventPaint);
        }
    }
}
