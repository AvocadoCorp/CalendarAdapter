package io.avocado.android.calendaradapter.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by matthewlogan on 5/22/14.
 */
public class CalendarCell extends LinearLayout{

    private Paint mEventPaint;

    private TextView mDateTextView;

    private int mNumEvents;

    public CalendarCell(Context context) {
        this(context, null);
    }

    public CalendarCell(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        int textSize = (int) context.getResources().getDimension(R.dimen.cell_text_size);

        mDateTextView = new TextView(context);
        LayoutParams dateTextLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mDateTextView.setLayoutParams(dateTextLp);
        mDateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        addView(mDateTextView);

        mEventPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEventPaint.setStyle(Paint.Style.FILL);
    }

    public void setTextColor(int textColor) {
        mDateTextView.setTextColor(textColor);
    }

    public void setTypeface(Typeface typeface) {
        mDateTextView.setTypeface(typeface);
    }

    public void setDateText(String dateText) {
        mDateTextView.setText(dateText);
    }

    public void setNumEvents(int numEvents) {
        mNumEvents = numEvents;
    }

    public void setEventColor(int eventColor) {
        mEventPaint.setColor(eventColor);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mNumEvents > 0) {
            int width = getWidth();
            int height = getHeight();

            int size = (int) (.15f * width);

            int x = (int) (width / 2.f - size / 2.f);

            int offsetFromBottom = (int) (.3f * height);

            int y = height - offsetFromBottom - size;

            canvas.drawRect(x, y, x + size, y + size, mEventPaint);
        }
    }
}
