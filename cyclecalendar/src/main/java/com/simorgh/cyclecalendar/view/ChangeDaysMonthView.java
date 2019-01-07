package com.simorgh.cyclecalendar.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.simorgh.cyclecalendar.R;

import java.util.Calendar;

import androidx.annotation.Nullable;

public class ChangeDaysMonthView extends BaseMonthView {
    //rectangle colors
    private static Paint rectTypeRedPaint;
    private static int rectTypeRedColor;

    private static Bitmap icon_check;

    private static boolean isInitialized = false;


    public ChangeDaysMonthView(Context context) {
        super(context);
    }

    public ChangeDaysMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangeDaysMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChangeDaysMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void initAttrs(Context context, AttributeSet attrs) {
        if (!isInitialized) {
            super.initAttrs(context, attrs);
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseMonthView);
            Resources resources = getResources();

            //circle colors
            rectTypeRedColor = resources.getColor(R.color.type_red);

            typedArray.recycle();
        }
    }

    @Override
    protected void initPaints() {
        if (!isInitialized) {
            super.initPaints();

            rectTypeRedPaint = new Paint();
            rectTypeRedPaint.setAntiAlias(true);
            rectTypeRedPaint.setStyle(Paint.Style.FILL);
            rectTypeRedPaint.setColor(rectTypeRedColor);

            rectTypeGrayPaint = new Paint();
            rectTypeGrayPaint.setAntiAlias(true);
            rectTypeGrayPaint.setStyle(Paint.Style.FILL);
            rectTypeGrayPaint.setColor(rectTypeGrayColor);

            icon_check = BitmapFactory.decodeResource(getResources(), R.drawable.icon_check);

            isInitialized = true;
        }
    }

    @Override
    protected void drawDays(Canvas canvas) {
        final TextPaint p = dayTextPaint;
        final int headerHeight = mMonthHeight;
        final int rowHeight = mDayHeight;
        final int colWidth = mCellWidth;

        int rowCenter = (int) (headerHeight + dp2px(8) + rowHeight / 2f);
        int left;
        int right;
        int top;
        int bottom;

        for (int day = 1, col = findDayOffset(); day <= mDaysInMonth; day++) {
            final int colCenter = colWidth * col + colWidth / 2;
            final int colCenterRtl;
            if (shouldBeRTL) {
                colCenterRtl = mPaddedWidth - colCenter;
            } else {
                colCenterRtl = colCenter;
            }

            date = getCalendarForDay(day);

            top = (int) (rowCenter - rowHeight / 2 + dp2px(3));
            bottom = (int) (rowCenter + rowHeight / 2 - dp2px(3));
            left = (int) (colCenterRtl - colWidth / 2 + dp2px(3));
            right = (int) (colCenterRtl + colWidth / 2 - dp2px(3));
            canvas.drawRect(left, top, right, bottom, getDayPaint(date));
            int dayType = getDayType(date);
            if (dayType == TYPE_GRAY) {
                dayTextPaint.setColor(tvMonthDayNumberTextColorBlack);
            } else {
                dayTextPaint.setColor(tvMonthDayNumberTextColorWhite);
            }
            canvas.drawText(mDayFormatter.format(day), right - p.getFontMetrics().descent - dp2px(6),
                    bottom - p.getFontMetrics().bottom, p);

            if (isValidDayOfMonth(day) && isDayInRangeSelectedListener.isDayInRangeSelected(date, cycleData)) {
                canvas.drawBitmap(icon_check, left + dp2px(10), top + dp2px(10), rectTypeRedPaint);
            }

            col++;
            if (col == DAYS_IN_WEEK) {
                col = 0;
                rowCenter += rowHeight;
            }
        }
    }

    @Override
    protected Paint getDayPaint(Calendar date) {
        if (cycleData == null) {
            return rectTypeGrayPaint;
        }
        if (isDayInRangeSelectedListener.isDayInRangeSelected(date, cycleData)) {
            return rectTypeRedPaint;
        }
        return rectTypeGrayPaint;
    }

    @Override
    protected int getDayType(Calendar date) {
        if (cycleData == null) {
            return TYPE_GRAY;
        }
        if (isDayInRangeSelectedListener.isDayInRangeSelected(date, cycleData)) {
            return TYPE_RED;
        }
        return TYPE_GRAY;
    }


}
