package com.simorgh.cluecalendar.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.simorgh.cluecalendar.R;

import java.util.Calendar;

import androidx.annotation.Nullable;

public class SetStartDayMonthView extends BaseMonthView {

    //rectangle colors
    private Paint rectTypeRedPaint;
    private Paint rectTypeGreenPaint;
    private Paint rectTypeYellowPaint;
    private Paint rectTypeMarkedPaint;
    private int rectTypeRedColor;
    private int rectTypeGreenColor;
    private int rectTypeGreen2Color;
    private int rectTypeYellowColor;
    private int rectTypeMarkedColor;

    private static Bitmap icon_check;


    public SetStartDayMonthView(Context context) {
        super(context);
    }

    public SetStartDayMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SetStartDayMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SetStartDayMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void initAttrs(Context context, AttributeSet attrs) {
        super.initAttrs(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseMonthView);
        Resources resources = getResources();

        //circle colors
        rectTypeRedColor = resources.getColor(R.color.type_red);
        rectTypeGreenColor = resources.getColor(R.color.type_green);
        rectTypeGreen2Color = resources.getColor(R.color.type_green);
        rectTypeYellowColor = resources.getColor(R.color.type_yellow);
        rectTypeMarkedColor = resources.getColor(R.color.type_marked);


        typedArray.recycle();
    }

    @Override
    protected void initPaints() {
        super.initPaints();

        rectTypeRedPaint = new Paint();
        rectTypeRedPaint.setAntiAlias(true);
        rectTypeRedPaint.setStyle(Paint.Style.FILL);
        rectTypeRedPaint.setColor(rectTypeRedColor);

        rectTypeGrayPaint = new Paint();
        rectTypeGrayPaint.setAntiAlias(true);
        rectTypeGrayPaint.setStyle(Paint.Style.FILL);
        rectTypeGrayPaint.setColor(rectTypeGrayColor);

        rectTypeGreenPaint = new Paint();
        rectTypeGreenPaint.setAntiAlias(true);
        rectTypeGreenPaint.setStyle(Paint.Style.FILL);
        rectTypeGreenPaint.setColor(rectTypeGreenColor);

        rectTypeYellowPaint = new Paint();
        rectTypeYellowPaint.setAntiAlias(true);
        rectTypeYellowPaint.setStyle(Paint.Style.FILL);
        rectTypeYellowPaint.setColor(rectTypeYellowColor);

        rectTypeMarkedPaint = new Paint();
        rectTypeMarkedPaint.setAntiAlias(true);
        rectTypeMarkedPaint.setStyle(Paint.Style.FILL);
        rectTypeMarkedPaint.setColor(rectTypeMarkedColor);

        icon_check = BitmapFactory.decodeResource(getResources(), R.drawable.icon_check);

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
            if (shouldBeRTL()) {
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

            if (isDaySelectedListener.isDaySelected(date)) {
                canvas.drawBitmap(icon_check, left + dp2px(10), top + dp2px(10), rectTypeRedPaint);
            }

            col++;
            if (col == DAYS_IN_WEEK) {
                col = 0;
                rowCenter += rowHeight;
            }
        }
    }

    public static int mathConstrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    @Override
    protected Paint getDayPaint(Calendar day) {
        if (clueData == null) {
            return rectTypeGrayPaint;
        }
        if (isDaySelectedListener.isDaySelected(date)) {
            return rectTypeRedPaint;
        }
        return rectTypeGrayPaint;
    }

    @Override
    protected int getDayType(Calendar date) {
        if (isDaySelectedListener.isDaySelected(date)) {
            return TYPE_RED;
        }
        return TYPE_GRAY;
    }
}
