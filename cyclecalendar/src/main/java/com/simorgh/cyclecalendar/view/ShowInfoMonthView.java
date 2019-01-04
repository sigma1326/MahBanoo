package com.simorgh.cyclecalendar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.cyclecalendar.R;
import com.simorgh.cycleutils.CycleData;

import java.util.Calendar;

import androidx.annotation.Nullable;

public class ShowInfoMonthView extends BaseMonthView {
    public static final String TAG = "monthView";

    //rectangle colors
    private static Paint rectTypeRedPaint;
    private static Paint rectTypeGreenPaint;
    private static Paint rectTypeYellowPaint;
    private static Paint rectTypeMarkedPaint;
    private static int rectTypeRedColor;
    private static int rectTypeGreenColor;
    private static int rectTypeYellowColor;
    private static int rectTypeMarkedColor;

    //Marked Triangle
    private Path markedPath;

    private static Bitmap icon;
    private IsDayMarkedListener isDayMarkedListener;

    public ShowInfoMonthView(Context context) {
        super(context);
        initAttrs(context, null);
        init();
    }

    public ShowInfoMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public ShowInfoMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShowInfoMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        init();
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
        rectTypeYellowColor = resources.getColor(R.color.type_yellow);
        rectTypeMarkedColor = resources.getColor(R.color.type_marked);


        typedArray.recycle();
    }

    @Override
    protected void initPaints() {
        super.initPaints();

        markedPath = new Path();

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

        icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
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

            if (isDayMarked(date)) {
                markedPath.moveTo(left, top);
                markedPath.lineTo(left + dp2px(14), top);
                markedPath.lineTo(left, top + dp2px(14));
                markedPath.lineTo(left, top);
                canvas.drawPath(markedPath, rectTypeMarkedPaint);
            }

            if (dayType == TYPE_GREEN2) {
                canvas.drawBitmap(icon, left + dp2px(7), top + dp2px(7), rectTypeGreenPaint);
            }

            col++;
            if (col == DAYS_IN_WEEK) {
                col = 0;
                rowCenter += rowHeight;
            }
        }
    }

    private boolean isDayMarked(Calendar date) {
        if (isDayMarkedListener != null) {
            return isDayMarkedListener.isDayMarked(date);
        }
        return false;
    }


    public static int mathConstrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    private static Calendar clearDate = Calendar.getInstance();
    @Override
    protected Paint getDayPaint(Calendar date) {
        if (cycleData == null || !showInfo) {
            return rectTypeGrayPaint;
        }
        long days;
        int day;
        CycleData tempCycle = null;
        clearDate.clear();
        if (cycleDataList == null || cycleDataList.isEmpty()) {
            return rectTypeGrayPaint;
        }

        for (CycleData cycleData : cycleDataList) {
            if (toTimeInMillis(cycleData.getEndDate()) == toTimeInMillis(clearDate)) {
                if (toTimeInMillis(date) >= toTimeInMillis(cycleData.getStartDate())) {
                    tempCycle = cycleData;
                    break;
                }
            } else {
                if (toTimeInMillis(date) >= toTimeInMillis(cycleData.getStartDate()) && toTimeInMillis(date) <= toTimeInMillis(cycleData.getEndDate())) {
                    tempCycle = cycleData;
                    break;
                }
            }
        }

        if (tempCycle == null) {
            return rectTypeGrayPaint;
        }

        days = CalendarTool.getDaysFromDiff(date, tempCycle.getStartDate());

        if (days >= 0) {
            day = (int) ((days) % tempCycle.getTotalDays()) + 1;
        } else {
            return rectTypeGrayPaint;
        }
        if (day <= tempCycle.getRedCount()) {
            return rectTypeRedPaint;
        } else if (day <= tempCycle.getTotalDays()) {
            if (day >= tempCycle.getGreenStartIndex() && day <= tempCycle.getGreenEndIndex()) {
                if (day == tempCycle.getGreen2Index()) {
                    return rectTypeGreenPaint;
                }
                return rectTypeGreenPaint;
            } else if (day >= tempCycle.getYellowStartIndex() && day <= tempCycle.getYellowEndIndex()) {
                return rectTypeYellowPaint;
            }
        }
        return rectTypeGrayPaint;
    }

    public IsDayMarkedListener getIsDayMarkedListener() {
        return isDayMarkedListener;
    }

    public void setIsDayMarkedListener(IsDayMarkedListener isDayMarkedListener) {
        this.isDayMarkedListener = isDayMarkedListener;
    }

    public interface IsDayMarkedListener {
        boolean isDayMarked(Calendar day);
    }
}
